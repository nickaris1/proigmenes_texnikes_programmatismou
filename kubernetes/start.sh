mkdir /mnt/data
cp ../database/database.db /mnt/data/database.db

kubectl apply -f ./pv-volume.yaml
kubectl apply -f ./pv-claim.yaml
kubectl apply -f ./deployment.yaml
kubectl apply -f ./service.yaml