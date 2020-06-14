FROM wordpress:5.4.1
RUN mkdir -p /usr/share/man/man1
RUN export DEBIAN_FRONTEND=noninteractive && apt-get -y update && apt-get install -y default-jre
RUN ln -s /etc/apache2/mods-available/cgi.load /etc/apache2/mods-enabled/cgi.load