const path = require("path");
const webpack = require("webpack");

var jsPath = path.resolve(__dirname, "src/main/webapp/js");

const config = {
	entry: {
		test1: path.resolve(jsPath, "src/test1.ts"),
	},
	output: {
		filename: '[name].js',
		path: path.resolve(jsPath, "dest")
	},
	mode: "development",
	resolve: {
		alias: {
			'vue': 'vue/dist/vue.js'
		},
		extensions: ['.ts', '.tsx', '.js']
	},
	optimization: {
		splitChunks: {
			chunks: 'all',
			name: "pub",
			cacheGroups: {}
		}
	},
	module: {
		rules: [
			// all files with a `.ts` extension will be handled by `ts-loader`
			{ test: /\.ts$/, loader: 'ts-loader' }
		]
	}
};

module.exports = config;
