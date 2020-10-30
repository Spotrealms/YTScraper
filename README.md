# YTScraper

A simple YouTube video scraper which collects video info using the video's page.

<a href="https://ci.spotrealms.com/view/1st-party/job/YTScraper/"><img alt="Jenkins Repository" src="https://ci.spotrealms.com/view/1st-party/job/YTScraper/badge/icon"></a>
<a href="https://github.com/Spotrealms/YTScraper/network"><img alt="GitHub Forks" src="https://badgen.net/github/forks/Spotrealms/YTScraper"></a>
<a href="https://github.com/Spotrealms/YTScraper/stargazers"><img alt="GitHub Stars" src="https://badgen.net/github/stars/Spotrealms/YTScraper"></a>
<a href="https://github.com/Spotrealms/YTScraper/issues"><img alt="GitHub Issues" src="https://badgen.net/github/issues/Spotrealms/YTScraper"></a>
<img alt="GitHub last commit" src="https://badgen.net/github/last-commit/Spotrealms/YTScraper">
<img alt="GitHub" src="https://badgen.net/github/license/Spotrealms/YTScraper">

### Features

- Multithreaded (up to 5 simultaneous threads for information processing)

- Allows processing of multiple video URLs in bulk

- Modern (supports Java >= 8 and uses OkHTTP in the backend)

### Supported Fields

- Video ID

- Video URL

- Video Title

- Video Duration (in milliseconds or HH:MM:SS)

- Upload Date (in "timecode format" or formatted string)

- Video Category

- Uploader (by name or ID)

- Video Keywords

### Getting Started

Note: For the fields marked with `VERSION`, replace that with the current version of the project, which can be found [here](VERSION).

#### Maven:

```xml
<project>
    ...
    <repositories>
        <repository>
            <id>spotrealmsci</id>
            <url>https://ci.spotrealms.com/plugin/repository/everything</url>
            <releases>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
    ...
    <dependencies>
        <dependency>
            <groupId>com.volcanicarts</groupId>
            <artifactId>ytscraper</artifactId>
            <version>VERSION</version>
        </dependency>
    </dependencies>
</project>
```

#### Gradle:

```gradle
repositories {
    maven {
        url "https://ci.spotrealms.com/plugin/repository/everything"
    }
}


dependencies {
    ytscraper "com.volcanicarts:ytscraper:VERSION"
}
```

#### Simple Example

```java
new YTScraper(uris).load(new VideoResultHandler(){
    @Override
    public void videoLoaded(YTVideo video){
        System.out.println("Title: " + video.getTitle());
    }

    @Override
    public void loadFailed(InvalidVideoException e){
        e.printStackTrace();
    }
});
```

For a more complete example, see [Main](src/test/Main.java).

### License

Apache 2 (see LICENSE)

### Special Thanks

VolcanicArts (for the upstream code)
