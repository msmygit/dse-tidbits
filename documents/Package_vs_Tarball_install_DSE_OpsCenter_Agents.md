# Tarball vs Package Installation of DSE, OpsCenter & Agents High level summary

**_NOTE:_** These are high level differences between package and tarball installations for DSE, OpsCenter & Agent

|Topics|Package Installation|Tarball Installation|
|:-------------:|:-------------:|:-----:|
|Requires Root access for the installation|Yes|No, can use any user|
|Service user running the process|DSE & Agents runs as `cassandra` and OpsCenter runs as `opscenter`|Configurable; any user could run|
|Can we modify the service user?|Maybe, more effort involved & not currently recommended|Yes, we could use any service user to run DSE/OpsCenter/Agents|
|Can we change default install directory for binaries|No|Yes, can be un-tarred into any directory location|
|Do we've a possibility to have multiple version in parallel?|No|Yes|
|OpsCenter Life Cycle Manager (LCM) Support. **Note** LCM currently supports only minor patch version upgrades|Yes|No|
|Service Initialization Script for start or stop actions|Yes, comes by default located at `/etc/init.d`|Does not come by default but, one could [create service initialization script](DataStax_Enterprise_Configuration_Directions_Tarball_Install.md) or use the regular start/stop methods (or) even set aliases in `.bash_profile` of the user|
|Ability to change default data and log locations|Yes|Yes|
|DSE Default File Locations|Refer to [DSE Package Install](https://docs.datastax.com/en/install/6.8/install/dsePackageLoc.html)|Refer to [DSE Tarball Install](https://docs.datastax.com/en/install/6.8/install/dseTarLoc.html)|
|OpsCenter Default File Locations|Refer to [OpsCenter Package Install](https://docs.datastax.com/en/opscenter/6.8/opsc/reference/opscDebianFileLocate_r.html)|Refer to [OpsCenter Tarball Install](https://docs.datastax.com/en/opscenter/6.8/opsc/reference/opscTarballFileLocate_r.html)|

---
