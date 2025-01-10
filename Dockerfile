# FROM openjdk:21-jdk-slim

# RUN apt-get update && apt-get install -y sudo



# Use a base image with Java and OpenVPN support
FROM ubuntu:20.04

# Set environment variables to suppress interactive prompts during installation
# ENV DEBIAN_FRONTEND=noninteractive

# Update and install required packages
RUN apt-get update && apt install -y \
    openvpn \
    iproute2 \
    iptables \
    && apt-get clean || cat /var/log/apt/term.log

# Ensure OpenVPN is available
RUN which openvpn

RUN useradd -m docker && echo "docker:docker" | chpasswd && adduser docker sudo

USER docker
CMD /bin/bash

COPY target/vpn-project-1.0.0.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Install EasyRSA for generating certificates (if needed)
# RUN wget -O /tmp/easyrsa.tgz https://github.com/OpenVPN/easy-rsa/releases/download/v3.1.5/EasyRSA-3.1.5.tgz \
 #   && tar -xvzf /tmp/easyrsa.tgz -C /usr/local/ \
  #  && rm /tmp/easyrsa.tgz

# Set up EasyRSA path
ENV PATH="/usr/local/EasyRSA-3.1.5:$PATH"

# Copy your OpenVPN server configuration and certificates
COPY certs/server.conf /etc/openvpn/server.conf
COPY certs/ca.crt /etc/openvpn/ca.crt
COPY certs/server.crt /etc/openvpn/server.crt
COPY certs/server.key /etc/openvpn/server.key
COPY certs/dh.pem /etc/openvpn/dh.pem

# Ensure proper permissions for sensitive files
# RUN chmod 600 /etc/openvpn/server.key \
    # && chmod 644 /etc/openvpn/server.crt /etc/openvpn/ca.crt /etc/openvpn/dh.pem /etc/openvpn/ta.key

# Expose the OpenVPN port (default 1194 UDP)
# EXPOSE 1194/udp

# Enable IP forwarding
# RUN echo "net.ipv4.ip_forward=1" >> /etc/sysctl.conf

# Create an entrypoint script for setting up OpenVPN
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

# Set the entrypoint
# ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]