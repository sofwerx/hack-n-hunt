## Build setup ##
* git clone https://github.com/AeroGlass/hack-n-hunt
* cd hack-n-hunt
* git submodule init
* git submodule update
* download, install and run Android Studio
* open hack-n-hunt project
* open Menu Bar > Tools > Android > SDK Manager
* install: Tools / Extras:
    Android SDK Build-tools 26.0.1
    Support Repository
    LLDB 2.3+
* install: SDK Platforms:
    Android 5.0.1 (API 21) - SDK Platform, Sources for Android SDK
    Android 4.4.2 (API 19) - SDK Platform, Sources for Android SDK
* select Menu Bar > Tools > Android > Sync Project with Gradle Files
* run Menu Bar > Build > Make Project

## Geopackage file ##
* geopackage file is: /sdcard/aeroglass/geopackage/hacknhunt-with-RTE.gpkg
* if geopackage file not exists example is created

## Basics ##
* when you start it you see "Waiting for location" on the screen while GPS is not ready or not precise enough
* short press pops up menu; you can select route here and others; see later
* route is a green trail
* POI is a blue-red marker; it has a label with name (if feature table doesn't have a "name" field, name is "poi #x" )
* CNP is a blue-gray bipyramid; it has a label with name + distance (if feature table doesn't have a "name" field, name is "aoi #x" )
* AOI is a cian colored area
* reference is like cnp, but has brow color; you can place it anywhere you are; only one reference is showed at a time

## Menu ##
* you can select route
* you can select CNP and see related images
* you can select distance units for labels
* you can turn on Look-ahead option; see later
* you can turn on Urban Mode; see later
* you can place Reference (it's useful when you use urban mode)
* you can exit from app
* (configs are saved when app closing in normal way)

## Look-ahead option ##
* "see next CNP and the one after that"
* "next CNP" is changed when you close enough to it (<20 m)
* stops when route finished
* you can select next CNP from menu
* menu show current next CNP and images when you open it
* it is enabled by default

## Urban Mode ##
* in urban environment GPS position and magnetic heading are able to be corrupted
* when you turn on "Urban Mode" it does two thinks; position calculated as meaning of last 10 position data; you can adjust heading by slide
* when you place Reference somewhere you can use it from a different point of view for adjust heading
* these effects are canceled when you leave this mode
* it is disabled by default

## Features for developers ##
* demo mode: set "HNHActivity.DEMO_MODE = true"; it moves around the selected route; you don't need live GPS data
* planet: set "HNHActivity.PLANET = true"; is show a map; you need internet connection; it is a base feature of G3M
* gpkg injection: you can inject all type of data from json files with "custom" suffixes; it deletes old "custom" tables first!

## How to inject data to gpkg using json files ##
* set "GeoPackageHelper.INJECT_JSON_TO_GPKG = true"
* create json files: [{"lat": ,"lng": }, ... ] for route, POI, CNP, AOI
* custom_route.json define a route
* custom_cnp.json define critical navigation points
* custom_poi.json define point of interests
* custom_aoi.json define an area (polygon)
* create image mapping file for CNP
* custom_cnp_image_map; seems like [{"id" : , "images" : [relative path 1, relative path 2, ...]},]
* check assets/custom_data for examples
* it can load it from assets/custom_data/ or /sdcard/aeroglass/custom_data/; when both of us are exist "sdcard has higher priority"
* it can load almost any type of images
* it has high chance for crashing at start, because we use different sql database for RTE support than ngageoint

## Known issues & incompletion ##
* Sometimes crashing at start
* It is not tested on tablet (Probably it has wrong orientation on tablets, because Default orientations are not the same.)
* There was no time and human resources for implementing IMU-GPS fusion with Kalman filter