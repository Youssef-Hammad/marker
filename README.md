# marker project repository

## Tablet of content
- [Installation](#Installation)
- [Documentation](#Documentation)
- [Coding Convention](#Coding-Convention)
- [Preview](#Preview)

# Installation
1. Clone the project
```
  git clone https://github.com/Youssef-Hammad/marker.git
```
2. Launch Android Studio
3. Open An Existing Project

OR,
1. Launch Android Studio
2. "Get from Version Control"
3. URL: https://github.com/Youssef-Hammad/marker.git

# Documentation
Documentation is automatically generated using Android Studio's [*javadoc*](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html) tool integration.

## Documentation Generation
To generate documentation, in Android Studio, goto `Tools -> Generate JavaDoc... -> OK`

## Viewing Documentation
Generated documentation will be outputted to `${PROJECT_DIR}/docs`

## Writing documentation
JavaDoc generates documentation based off of comments starting with a `/**` and ending with a `*/`
### An Example
```java
/**
 * Returns an Image object that can then be painted on the screen. 
 * The url argument must specify an absolute <a href="#{@link}">{@link URL}</a>. The name
 * argument is a specifier that is relative to the url argument. 
 * <p>
 * This method always returns immediately, whether or not the 
 * image exists. When this applet attempts to draw the image on
 * the screen, the data will be loaded. The graphics primitives 
 * that draw the image will incrementally paint on the screen. 
 *
 * @param  url  an absolute URL giving the base location of the image
 * @param  name the location of the image, relative to the url argument
 * @return      the image at the specified URL
 * @see         Image
 */
public Image getImage(URL url, String name) {
  try {
    return getImage(new URL(url, name));
  } catch (MalformedURLException e) {
    return null;
  }
}
```

# Coding Convention
[The Java Coding Convention](https://en.wikibooks.org/wiki/Java_Programming/Coding_conventions#:~:text=Documentation%20should%20always%20accompany%20code,final%20field%20in%20a%20class.) is used.

# Preview
![Super Heroes](https://i.ibb.co/C1frKt0/67f4292d-0cd3-45d0-98c8-db344a5ac8a2.jpg)
![Digimon Leomon](https://i.ibb.co/fvwVsmc/44279401-fb9f-4876-ba9d-836d678c2e46.jpg)
