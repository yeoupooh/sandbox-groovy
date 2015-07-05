#!/bin/bash

keytool -importcert -alias "youralias" -file api.telegram.org.cer -keystore truststore.jks -storepass password