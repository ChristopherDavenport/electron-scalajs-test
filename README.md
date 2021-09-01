# electron-test - Electron Test App 

## Working With Electron

Electron works much like a chrome browser. We present a minimal picture of how one does this

- main.js: This is the central Electron process whose job it is to manage all things related to windows and upper level constructs.
- renderer.js: This is the process responsible for the maintenance of the page. It works like any other dom based script and only has access to the dom environment like in a browser window, it may communicate with the electron process via event handlers and via ipcMain which is a pipeline between the main and renderer processes. This is loaded like a normal script from a web resource.
- preload.js: This operates to allow communication with both the dom and node environments. May not be necessary for many use cases. 
- output/index.html: This is the base document, it works and loads like a traditional webpage.