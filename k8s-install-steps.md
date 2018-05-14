1. go through https://kubernetes.io/docs/setup/independent/install-kubeadm/
2. go through http://blog.csdn.net/u012375924/article/details/78987263
3. go through if need logging http://blog.csdn.net/aixiaoyang168/article/details/78454927
3. record ip address
   1. ifconfig -a
    '''  192.168.5.47 '''
4. check product_uuid, make sure uniqueness
  sudo cat /sys/class/dmi/id/product_uuid
    '''  B1C9BC39-A247-429C-86B3-270A887B2F43  '''
5. verify ports [6443, 10251] open
   1. firewall-cmd --list-ports
   2. firewall-cmd --add-port
   
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
   2. yum install -y kubelet-1.9.6 kubectl-1.9.6 kubeadm-1.9.6
   3. setenforce 0
   4. cat > /etc/systemd/system/kubelet.service.d/20-pod-infra-image.conf <<EOF
   [Service]
   Environment="KUBELET_EXTRA_ARGS=--pod-infra-container-image=registry.cn-hangzhou.aliyuncs.com/maycur-k8s/pause-amd64:3.1"
   EOF
   
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
15. logging configuration
    1. git clone https://github.com/kubernetes/kubernetes.git
    2. cd kubernetes
    3. git checkout v1.9.3
    4. cd cluster/addons/fluentd-elasticsearch
    5. change log-driver to json-file: vi /etc/sysconfig/docker  
       
       ` OPTIONS='--selinux-enabled --log-driver=json-file --signature-verification=false' `
    6. systemctl daemon-reload && systemctl restart docker
    7. cd kubernetes/cluster/addons/fluentd-elasticsearch
    8. kubectl apply -f fluentd-es-configmap.yaml
    9. kubectl apply -f fluentd-es-ds.yaml
    10. kubectl label node kuber beta.kubernetes.io/fluentd-ds-ready=true
    11. kubectl apply -f es-statefulset.yaml
    12. kubectl apply -f es-service.yaml 
    13. kubectl apply -f kibana-deployment.yaml
    14. kubectl apply -f kibana-service.yaml
    ～～ 15. kubectl proxy --address='192.168.5.15' --port=8085 --accept-hosts='^*$' ～～
16. node rejoin cluster (https://stackoverflow.com/questions/47126779/join-cluster-after-init-token-expired)
    1. login to master node
    2. kubeadm token create
    3. openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* //'
    4. node join the kubeadm join cmd
    ` kubeadm join --token abcdef.1234567890abcdef --discovery-token-ca-cert-hash sha256:e18105ef24bacebb23d694dad491e8ef1c2ea9ade944e784b1f03a15a0d5ecea 1.2.3.4:6443`
17. install heapster to monitor resource usage:
    1. https://github.com/kubernetes/heapster/blob/master/docs/influxdb.md

18. ingress
    1. 安装nginx-ingress-controller, 有两个地方需要主要， 
       1. 要配置hostNetwork: true
       2. 要配置serviceAccountName.
    2. ingress 貌似不能支持同时配置http/https
19. push to private docker registry
    1. docker login
    2. mvn setting.xml configure sever
    3. pom.xml indicate serverId
20. check service
    1. kubectl run curl --image=radial/busyboxplus:curl -i --tty
    2. nslookup config-server
