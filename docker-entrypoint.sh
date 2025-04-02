#!/bin/sh
if [ -f /etc/secrets/ca.pem ]; then
    echo "Configurando certificado do Render..."
    cp /etc/secrets/ca.pem /usr/local/share/ca-certificates/render-ca.crt
    chmod 644 /usr/local/share/ca-certificates/render-ca.crt
    update-ca-certificates

    keytool -importcert -noprompt \
            -keystore $JAVA_HOME/lib/security/cacerts \
            -storepass changeit \
            -file /etc/secrets/ca.pem \
            -alias render-ca
fi

exec "$@"