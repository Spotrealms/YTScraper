# YTScraper

A simple YouTube video scraper which collects video info using the video's page. Based on VolcanicArts' project by the same name.

<a href="https://ci.spotrealms.com/view/1st-party/job/YTScraper/"><img alt="Jenkins Repository" src="https://ci.spotrealms.com/view/1st-party/job/YTScraper/badge/icon"></a>
<a href="https://github.com/Spotrealms/YTScraper/network"><img alt="GitHub Forks" src="https://badgen.net/github/forks/Spotrealms/YTScraper"></a>
<a href="https://github.com/Spotrealms/YTScraper/stargazers"><img alt="GitHub Stars" src="https://badgen.net/github/stars/Spotrealms/YTScraper"></a>
<a href="https://github.com/Spotrealms/YTScraper/issues"><img alt="GitHub Issues" src="https://badgen.net/github/issues/Spotrealms/YTScraper"></a>
<img alt="GitHub last commit" src="https://badgen.net/github/last-commit/Spotrealms/YTScraper">
<img alt="GitHub" src="https://badgen.net/github/license/Spotrealms/YTScraper">

### Why?

This fork was created because the project, at the time of forking, lacked some of the features that were deemed essential like the video uploader field. In addition, the project was targeting Java 11 and newer. While Java 11 is now in mainstream LTS support and Java 8 is now EOL, widespread adoption of Java 11 has not yet been achieved, rendering this project unable to be used in JREs that aren't up to date. Thus, this fork has been continued such that Java 8 can be supported along with JREs running version 11 and newer. To achieve this, Java 11's HttpClient was replaced with OkHttp, which still supports Java 8 unlike the former. To the best of my knowledge, this project should perform about the same as the upstream, albeit running on Java 8 instead of Java 11 in some cases.

### Features

- Multithreaded (up to 5 simultaneous threads for information processing)
- Allows processing of multiple video URLs in bulk
- Modern (supports Java >= 8 and uses OkHTTP in the backend)

### Supported Fields

- Video ID
- URL
- Title
- Description
- Duration (in milliseconds or HH:MM:SS)
- Thumbnail URLs (whole list or by type; eg: maxresdefault)
- Uploader (by name or ID)
- Uploader URL
- Upload Date (in "timecode format" or formatted string)
- Category
- Keywords
- Available Countries (ie: where the video can be viewed)
- View Count (raw or formatted)
- Whether or not ratings are allowed
- Likes, dislikes, and the total ratio (formatted and unformatted for each; -1 for all 3 if the ratings have been disabled)
- Whether or not the video is unlisted
- Whether or not the video is crawlable by search engines
- Whether or not the video is private
- Whether or not the video is age restricted
- Whether or not the video is available in YouTube Kids
- Whether or not the video is live content
- Whether or not the video has the "unplugged corpus" status, though the true function of this flag is unknown
- Full JSON for both the player data and render data

### Getting Started

Note: For the fields marked with `VERSION`, replace that with the current version of the project, which can be found [here](VERSION).

#### Maven

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

#### Gradle

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
new YTScraper(uri).load(new VideoResultHandler(){
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

Apache 2 (see [LICENSE](LICENSE))

### Special Thanks

VolcanicArts (for the upstream code)
