# payment

### Azure Group ###
az group create --name=pay-bank-grp --location=brazilsouth

az group delete --name=pay-bank-grp

### Azure Group ###
### Criar o banco de dados ###
AZ_RESOURCE_GROUP=pay-bank-grp
AZ_DATABASE_NAME=pay-bank-db
AZ_LOCATION=brazilsouth
AZ_POSTGRESQL_USERNAME=spring
AZ_POSTGRESQL_PASSWORD=
AZ_LOCAL_IP_ADDRESS=177.19.82.104

->Create an Azure Database for PostgreSQL instance
az postgres server create \
    --resource-group pay-bank-grp \
    --name pay-bank-db \
    --location brazilsouth \
    --sku-name B_Gen5_1 \
    --storage-size 5120 \
    --admin-user spring \
    --admin-password $AZ_POSTGRESQL_PASSWORD
	
->Configure a firewall rule for your PostgreSQL server	
az postgres server firewall-rule create \
    --resource-group pay-bank-grp \
    --name pay-bank-db-database-allow-local-ip \
    --server pay-bank-db \
    --start-ip-address 177.19.82.104 \
    --end-ip-address 177.19.82.104
	
az postgres server firewall-rule create \
    --resource-group pay-bank-grp \
    --name pay-bank-db-database-kubernetes \
    --server pay-bank-db \
    --start-ip-address 191.235.240.0 \
    --end-ip-address 191.235.240.255
	
	
->Configure a PostgreSQL database
az postgres db create \
    --resource-group pay-bank-grp \
    --name banco \
    --server-name pay-bank-db

### Criar o banco de dados ###

## AKS ##

az acr create --resource-group pay-bank-grp --location brazilsouth --name pay-bank-registry --sku Basic
az acr login --name pay-bank-registry
docker tag pay-bank-app:latest pay-bank-registry.azurecr.io/pay-bank-app:latest
docker push pay-bank-registry.azurecr.io/pay-bank-app:latest

az aks create -g pay-bank-grp -n pay-bank-cluster --kubernetes-version 1.17.11 --node-count 2 --node-vm-size Standard_DS2_v2 --nodepool-name slocpool --attach-acr pay-bank-registry --dns-name-prefix=pay-bank-app-kubernetes --generate-ssh-keys
az aks install-cli
az aks get-credentials --resource-group=pay-bank-grp --name=pay-bank-cluster
kubectl run pay-bank-app-docker --image=pay-bank-registry.azurecr.io/pay-bank-app:latest
kubectl expose pod pay-bank-app-docker --type=LoadBalancer --port=80 --target-port=8090
kubectl expose pod pay-bank-app-docker --type=LoadBalancer --port=8081 --target-port=8081
kubectl get services -o=jsonpath='{.items[*].status.loadBalancer.ingress[0].ip}'





### Ver a questão do volume ###
### Tentar montar um volume ###

https://dev.azure.com/mathcunha/
https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-data-jpa-with-azure-postgresql
https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/deploy-spring-boot-java-app-on-kubernetes
