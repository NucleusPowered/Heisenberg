/*
 * This file is part of heisenberg, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleusheisenberg.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GeoIpConfig {

    // Not localised, this is a legal statement that should be adhered to.
    @Setting(value = "accept-licences", comment = "By setting this to true, you agree to the licences for the MaxMind GeoLite 2 databases, and the information as displayed at http://nucleuspowered.org/thirdparty/geoip.html \nor in the geoip.txt file in the plugin JAR (you can open the Nucleus JAR with any zip program)")
    private boolean acceptLicence = false;

    @Setting(value = "alert-on-login", comment = "If true, Nucleus will tell players with the \"heisenberg.login\" permission the country the "
            + "player is connecting via. Please be considerate with this permission, do not give it to everyone.")
    private boolean alertOnLogin = false;

    @Setting(value = "country-data", comment = "The URL to get GeoLite2 databases from. Do not change this unless necessary.")
    private String countryUrl = "http://geolite.maxmind.com/download/geoip/database/GeoLite2-Country.mmdb.gz";

    public boolean isAcceptLicence() {
        return this.acceptLicence;
    }

    public boolean isAlertOnLogin() {
        return this.alertOnLogin;
    }

    public String getCountryData() {
        return this.countryUrl;
    }
}
