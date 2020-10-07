# Payment
This repo aims to provide a Spring Boot App that uses PostgreSQL to persist data, also stores data at the file system. The app dependencies are managed by maven.
Besides the actual app implementation, this repo presents instructions to provisioning Azure resources used to deploy a Spring Boot Application at Azure AKS.

## Azure resources
### Prerequisites
* Clone this repo;
* An Azure subscription;
* The Azure Command-Line Interface (CLI);
* A Git client;
* A Docker client.

### Create an Azure Resource Group
1. Log in to Azure account:
```
az login
```
2. Create a resource group for the Azure resources used in this tutorial:
```
az group create --name=pay-bank-grp --location=brazilsouth
```
3. Check the recent created resource group:
```
az group list
```
You shall see an resource group array.
### Create an Azure Database for PostgreSQL instance
1. Set environment variables:
```
export AZ_RESOURCE_GROUP=pay-bank-grp
export AZ_DATABASE_NAME=pay-bank-db
export AZ_LOCATION=brazilsouth
export AZ_POSTGRESQL_USERNAME=spring
export AZ_POSTGRESQL_PASSWORD=
export AZ_LOCAL_IP_ADDRESS=<YOUR_IP>
```
* <YOUR_IP>: The IP address of your local computer, from which you'll run your Spring Boot application. One convenient way to find it is to point your browser to whatismyip.akamai.com.

2. Create an Azure Database for PostgreSQL instance:
```
az postgres server create \
    --resource-group pay-bank-grp \
    --name pay-bank-db \
    --location brazilsouth \
    --sku-name B_Gen5_1 \
    --storage-size 5120 \
    --admin-user spring \
    --admin-password $AZ_POSTGRESQL_PASSWORD
```
3. Configure a firewall rule for your PostgreSQL server:
```
az postgres server firewall-rule create \
    --resource-group pay-bank-grp \
    --name pay-bank-db-database-allow-local-ip \
    --server pay-bank-db \
    --start-ip-address <YOUR_IP> \
    --end-ip-address <YOUR_IP>
```
4.  Configure a PostgreSQL database:
```
az postgres db create \
    --resource-group pay-bank-grp \
    --name banco \
    --server-name pay-bank-db
```
At that step you are able to connect at your Azure Database for PostgreSQL at jdbc:postgresql://pay-bank-db.postgres.database.azure.com:5432/{your_database}?user=spring@pay-bank-db&password={your_password}&sslmode=require.
Note that I had to add gssEncMode=disable parameters to the connection string due to an EOF error.

### Create an Azure Container Registry using the Azure CLI
1. Build app Docker Image using provided [Dockerfile](./Dockerfile):
```
docker build -t pay-bank-app:latest ./ -f Dockerfile
```
2. Create an Azure Container Registry using the Azure CLI:
```
az acr create --resource-group pay-bank-grp --location brazilsouth --name paybankregistry --sku Basic
```
3. Login to Azure Container Registry
```
az acr login --name paybankregistry
```
4. Upload your Docker Image:
```
docker tag pay-bank-app:latest paybankregistry.azurecr.io/pay-bank-app:latest
docker push paybankregistry.azurecr.io/pay-bank-app:latest
```
At that step your app image was uploaded to your Azure Container Registry.
### Kubernetes Cluster on Azure Kubernetes Service (AKS)
Now will be created a Kubernetes Cluster. This is being done now, because the app needs to mount a volume and the Kubernetes Cluster name is a prerequisite to create a new volume.
```
az aks create -g pay-bank-grp -n pay-bank-cluster --kubernetes-version 1.17.11 --node-count 2 --node-vm-size Standard_DS2_v2 --nodepool-name bankpool --attach-acr paybankregistry --dns-name-prefix=pay-bank-app-kubernetes --generate-ssh-keys
```

### Create an Azure disk
The following example gets the node resource group for the AKS cluster name pay-bank-cluster in the resource group name pay-bank-grp:
```
az aks show --resource-group pay-bank-grp --name pay-bank-cluster --query nodeResourceGroup -o tsv
```
Now create a disk using the az disk create command. Specify the node resource group name obtained in the previous command:
```
az disk create \
  --resource-group MC_SlcGroup_SlcCluster_brazilsouth \
  --name myAKSDisk \
  --size-gb 20 \
  --query id --output tsv
```
The disk resource ID is displayed once the command has successfully completed, as shown in the following example output. This disk ID is used to mount the disk. Replace that information at the end of file [Kubernetes File](./payment-deployment.yml)
```
/subscriptions/<subscriptionID>/resourceGroups/MC_myAKSCluster_myAKSCluster_eastus/providers/Microsoft.Compute/disks/myAKSDisk
```



### Kubernetes Cluster on Azure Kubernetes Service (AKS)
1. Install kubectl using the Azure CLI. Linux users may have to prefix this command with sudo since it deploys the Kubernetes CLI to /usr/local/bin.
```
az aks install-cli
```
2. Download the cluster configuration information so you can manage your cluster from the Kubernetes web interface and kubectl.
```
az aks get-credentials --resource-group=pay-bank-grp --name=pay-bank-cluster
```
3. Register database password as secret.
```
kubectl create secret generic postgres-pass --from-literal=password=<AZ_POSTGRESQL_PASSWORD>
```
4. If you need, update the database JDBC_DATABASE_URL and JDBC_DATABASE_USERNAME at [Kubernetes File](./payment-deployment.yml) with data.
5. Deploy the app on your AKS cluster:
```
kubectl apply -f payment-deployment.yml
```
6. Wait until your pod is Running
```
kubectl get pods
```
If you get an error, should be because of the database firewall. Make sure to enable connection from you AKS cluster sing the command below with your AKS ip address.
```
az postgres server firewall-rule create \
    --resource-group pay-bank-grp \
    --name pay-bank-db-database-kubernetes \
    --server pay-bank-db \
    --start-ip-address 191.235.113.0 \
    --end-ip-address 191.235.113.255
```
### Acessing the application
1. Identify external ip address?
```
kubectl get services
```
2. Curl your app
```
curl -X POST \
  http://<EXTERNAL_IP>/api/v1/arquivosCnab \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"nome": "file.txt"
}'
```
3. List mounted file system data
```
curl http://<EXTERNAL_IP>/api/v1/localFiles
```

### Clean up resources
In the preceding steps, you created Azure resources in a resource group. If you don't expect to need these resources in the future, delete the resource group from portal, or by running the following command in the Cloud Shell:
```
az group delete --resource-group pay-bank-grp --subscription <SUBSCRIPTION> --yes
```
### Config Azure DevOps
You can use [azure-pipeline.yml](./azure-pipeline.yml) to enable your repo CI/CD.
### Source
* https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-data-jpa-with-azure-postgresql
* https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/deploy-spring-boot-java-app-on-kubernetes
* https://docs.microsoft.com/en-us/azure/aks/azure-disk-volume
