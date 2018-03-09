1. go through https://kubernetes.io/docs/setup/independent/install-kubeadm/
2. go through http://blog.csdn.net/u012375924/article/details/78987263
3. record ip address
   1. ifconfig -a
    '''  192.168.5.47 '''
4. check product_uuid, make sure uniqueness
  sudo cat /sys/class/dmi/id/product_uuid
    '''  B1C9BC39-A247-429C-86B3-270A887B2F43  '''
5. verify ports [6443, 10251] open
   1. firewall-cmd --list-ports
   2. firewall-cmd --add-port=10250/tcp --permanent
   3. firewall-cmd —reload
   4. firewall-cmd --add-port=portid-portid/protocol
   5. service firewalld stop
6. check available RAM
   1. free -m
7. stop swap
   1. swapoff -a
8. install docker
   1. yum install -y docker
   2. systemctl enable docker && systemctl start docker
   3. config docker http & https proxy
      1. mkdir -p /etc/systemd/system/docker.service.d
      2. vi /etc/systemd/system/docker.service.d/http-proxy.conf
      [Service]
      Environment="HTTP_PROXY=http://127.0.0.1:8118" "NO_PROXY=localhost,172.16.0.0/16,127.0.0.1,10.244.0.0/16"
      3. vi /etc/systemd/system/docker.service.d/https-proxy.conf
      [Service]
      Environment="HTTPS_PROXY=http://127.0.0.1:8118" "NO_PROXY=localhost,172.16.0.0/16,127.0.0.1,10.244.0.0/16"
   4. systemctl daemon-reload && systemctl restart docker
9. Some users on RHEL/CentOS 7 have reported issues with traffic being routed incorrectly due to iptables being bypassed. You should ensure net.bridge.bridge-nf-call-iptables is set to 1 in your sysctl config, e.g.
   1. cat <<EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
   2. sysctl --system
10. install kubeadm, kubelet & kubectl
   1. set yum source for kubenet
   vi /etc/yum.repos.d/kubernetes.repo
   [kubernetes]
   name=Kubernetes
   baseurl=http://yum.kubernetes.io/repos/kubernetes-el7-x86_64
   enabled=1
   gpgcheck=0
   [kubernetes]
   name=Kubernetes
   baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
   enabled=1
   gpgcheck=0
   2. yum install -y kubelet kubeadm kubectl
   3. setenforce 0
   4. systemctl enable kubelet && sudo systemctl start kubelet
11. initialize kubenetes cluster
   1. master
      kubeadm init --apiserver-advertise-address=192.168.5.15 --pod-network-cidr=10.244.0.0/16
   2. record the output like following:
   kubeadm join --token f66101.fa35a1fac86f27cb 192.168.5.15:6443 --discovery-token-ca-cert-hash sha256:824879623fec10c7aa118f3dd6a958462194accf9e11db89c29daea7ed4fd03e
   3. export KUBECONFIG=/etc/kubernetes/admin.conf
   4. echo "export KUBECONFIG=/etc/kubernetes/admin.conf" >> ~/.bash_profile
12. add network addones (choose either one)
   1. kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
   2. kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/v0.9.1/Documentation/kube-flannel.yml
13. join worker nodes
    1. kubeadm join --token 2fae1b.e4306e679794caaf 172.16.93.220:6443 --discovery-token-ca-cert-hash sha256:26d12811d5a60ecd91ba6bfea03daa34460d6143ceb11777179fd02e09f0ad16
14. install kubernates dashboard
   token:      eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC1hZG1pbi10b2tlbi1meDdxNyIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC1hZG1pbiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjIyOGZkODNhLTIyYzEtMTFlOC1hMWEyLWY4ZGI4OGZmNjExNCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWRhc2hib2FyZC1hZG1pbiJ9.C_wjIh1J2hbWEp-kCtXf6ftYJ7Ecc6S0xFaJwekk0DPf-TBVUGWPqBhJS2tAblmh6HVpg7fgWAmkD5FqvFudTIThMAYt_956E7tgTStMNj_NCUlvbTj7knh-d-TdJQlzfkNcvXBYP0cTWyKFP0jAmIIEFoWyD9KGqRvNzRaZh9eW3cu3k7SjXg6vtDg0ma8lhq5FY25edIhBsawnMGSedsyPCDsm8KvcJCs81rwvIwRPq_yaja1tWWvjqKAdX1hIBQVV6MkZezgWvSRKwoI3Si89W8WTxXdjDzQuXFaGve64nZX0vWPNSidxAlKj9ptuZA-5I6Z1vkcYrncoAUa_xw
15.
