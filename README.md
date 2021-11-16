# fxhash + shadow-cljs + quil quickstart

A light `shadow-cljs` template to create generative art with [quil](http://quil.info/) for the [fxhash](https://www.fxhash.xyz/) platform. 

You'll need `Java`, `node` and `npm` installed (`yarn` also works). 

# Setup + Development

`npm i` or `yarn` to install dependencies

`npm run dev` launches a live-reloading dev environment at `http://localhost:8020/`

## Calva

This project works with `calva`, make sure to select `shadow-cljs` then check the box next to `:app` and connect to `:app` for your REPL.

## fxhash

The fxhash host exposes two useful variables, `fxhash` and `fxrand`, which are clojureified in the `starter.fx-hash` namespace as `(fx-hash)` and `(fx-rand)`.

See the [fxhash guide](https://www.fxhash.xyz/articles/guide-mint-generative-token).

# Packaging + Release

`npm run release` will build & package into `fxhash-release.zip`, ready to upload to the site.

# Tips

It's a regular quil sketch, so refer to the [quil API docs](http://quil.info/api) and the [fxhash guide](https://www.fxhash.xyz/articles/guide-mint-generative-token) for further guidance.