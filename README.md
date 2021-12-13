# digital city

A light `shadow-cljs` template to create generative art with [quil](http://quil.info/) for the [fxhash](https://www.fxhash.xyz/) platform. 

You'll need `Java`, `node` and `npm` installed (`yarn` also works). 

![image](https://user-images.githubusercontent.com/5009316/145823505-70220b0a-d347-46d2-b6f2-a4fc17f01137.png)


# Setup + Development

`npm i` or `yarn` to install dependencies

`npm run dev` launches a live-reloading dev environment at `http://localhost:8020/`

### Calva

This project works with `calva`, make sure to select `shadow-cljs` then check the box next to `:app` and connect to `:app` for your REPL.

### fxhash

The fxhash host exposes two useful variables, `fxhash` and `fxrand`, which are clojureified in the `starter.fx-hash` namespace as `(fx-hash)` and `(fx-rand)`.

See the [fxhash guide](https://www.fxhash.xyz/articles/guide-mint-generative-token).

We also expose a method to register features of your token via `(register-features {:feat-a true})` in the same namespace.

### index.html + main.css

Both these files (located in the `public/` folder) can be edited freely so long as you preserve the `fxhash` snippet and the filepaths.

If you rename the root namespace from `starter` make sure to update the call to `starter.quil.init()` in `index.html`.

## Packaging + Release

`npm run release` will build & package into `fxhash-release.zip`, ready to upload to the site.

## Tips

It's a regular quil sketch, so refer to the [quil API docs](http://quil.info/api) and the [fxhash guide](https://www.fxhash.xyz/articles/guide-mint-generative-token) for further guidance.
