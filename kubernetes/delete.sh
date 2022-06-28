kubectl delete -f ./service.yaml
kubectl delete -f ./deployment.yaml
kubectl delete -f ./pv-claim.yaml
kubectl delete -f ./pv-volume.yaml

rm -rf /mnt/data