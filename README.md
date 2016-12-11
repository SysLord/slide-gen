# slide-gen

Goal is to implement a pure java dynamic content image creation kit, with graphical editor, custom layouter and image generation with awt.
Dynamic content means the support for different text lengths and ellipsis.

## technologies
- Java
- AWT
- Vaadin
- Spring Boot

## Build and run
I should be enough to build the root pom.xml with maven.
Then run class SlideEditorApplication.
Open browser with localhost:8080


## Features TODO 
* Fix vaadin windows in chrome. page is scrolled down. Maybe update vaadin version?
* Boxes could be able to pop in and out of container boxes by keyboard shortcuts
* Write/edit text directly in editor? Anyway the content textfield needs to be a textarea.
* autorender each X seconds
* lines
* different fonts
* show box-tree as this will be important for rendering
* OR maybe make sure that for rendering boxes are sorted by y ascending?
* background image
* insert image
* import LayoutableBox into UI
* streamline all configuration parameters
* textfields must support placeholders/properties and will be filled by a bean or a value map
* multiple render outputs with large and tiny texts, set of example data should be part of the slide
* background color
* padding and margin
* borders
