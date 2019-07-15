/******/ (function(modules) { // webpackBootstrap
/******/ 	// install a JSONP callback for chunk loading
/******/ 	function webpackJsonpCallback(data) {
/******/ 		var chunkIds = data[0];
/******/ 		var moreModules = data[1];
/******/ 		var executeModules = data[2];
/******/
/******/ 		// add "moreModules" to the modules object,
/******/ 		// then flag all "chunkIds" as loaded and fire callback
/******/ 		var moduleId, chunkId, i = 0, resolves = [];
/******/ 		for(;i < chunkIds.length; i++) {
/******/ 			chunkId = chunkIds[i];
/******/ 			if(installedChunks[chunkId]) {
/******/ 				resolves.push(installedChunks[chunkId][0]);
/******/ 			}
/******/ 			installedChunks[chunkId] = 0;
/******/ 		}
/******/ 		for(moduleId in moreModules) {
/******/ 			if(Object.prototype.hasOwnProperty.call(moreModules, moduleId)) {
/******/ 				modules[moduleId] = moreModules[moduleId];
/******/ 			}
/******/ 		}
/******/ 		if(parentJsonpFunction) parentJsonpFunction(data);
/******/
/******/ 		while(resolves.length) {
/******/ 			resolves.shift()();
/******/ 		}
/******/
/******/ 		// add entry modules from loaded chunk to deferred list
/******/ 		deferredModules.push.apply(deferredModules, executeModules || []);
/******/
/******/ 		// run deferred modules when all chunks ready
/******/ 		return checkDeferredModules();
/******/ 	};
/******/ 	function checkDeferredModules() {
/******/ 		var result;
/******/ 		for(var i = 0; i < deferredModules.length; i++) {
/******/ 			var deferredModule = deferredModules[i];
/******/ 			var fulfilled = true;
/******/ 			for(var j = 1; j < deferredModule.length; j++) {
/******/ 				var depId = deferredModule[j];
/******/ 				if(installedChunks[depId] !== 0) fulfilled = false;
/******/ 			}
/******/ 			if(fulfilled) {
/******/ 				deferredModules.splice(i--, 1);
/******/ 				result = __webpack_require__(__webpack_require__.s = deferredModule[0]);
/******/ 			}
/******/ 		}
/******/ 		return result;
/******/ 	}
/******/
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// object to store loaded and loading chunks
/******/ 	// undefined = chunk not loaded, null = chunk preloaded/prefetched
/******/ 	// Promise = chunk loading, 0 = chunk loaded
/******/ 	var installedChunks = {
/******/ 		"test1": 0
/******/ 	};
/******/
/******/ 	var deferredModules = [];
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	var jsonpArray = window["webpackJsonp"] = window["webpackJsonp"] || [];
/******/ 	var oldJsonpFunction = jsonpArray.push.bind(jsonpArray);
/******/ 	jsonpArray.push = webpackJsonpCallback;
/******/ 	jsonpArray = jsonpArray.slice();
/******/ 	for(var i = 0; i < jsonpArray.length; i++) webpackJsonpCallback(jsonpArray[i]);
/******/ 	var parentJsonpFunction = oldJsonpFunction;
/******/
/******/
/******/ 	// add entry module to deferred list
/******/ 	deferredModules.push(["./web/src/main/webapp/js/src/test1.ts","pub"]);
/******/ 	// run deferred modules when ready
/******/ 	return checkDeferredModules();
/******/ })
/************************************************************************/
/******/ ({

/***/ "./web/src/main/webapp/js/src/test1.ts":
/*!*********************************************!*\
  !*** ./web/src/main/webapp/js/src/test1.ts ***!
  \*********************************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var jquery__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! jquery */ \"./node_modules/_jquery@3.4.0@jquery/dist/jquery.js\");\n/* harmony import */ var jquery__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(jquery__WEBPACK_IMPORTED_MODULE_0__);\n/* harmony import */ var _tsui__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./tsui */ \"./web/src/main/webapp/js/src/tsui.ts\");\n\r\n\r\njquery__WEBPACK_IMPORTED_MODULE_0___default()(document).ready(function (el) {\r\n    _tsui__WEBPACK_IMPORTED_MODULE_1__[\"UIManager\"].Init();\r\n    var cols = new _tsui__WEBPACK_IMPORTED_MODULE_1__[\"CowContainer\"](30, 70);\r\n    _tsui__WEBPACK_IMPORTED_MODULE_1__[\"stage\"].addChild(cols);\r\n});\r\n\n\n//# sourceURL=webpack:///./web/src/main/webapp/js/src/test1.ts?");

/***/ }),

/***/ "./web/src/main/webapp/js/src/tsui.ts":
/*!********************************************!*\
  !*** ./web/src/main/webapp/js/src/tsui.ts ***!
  \********************************************/
/*! exports provided: UIManager, CowContainer, ColItem, ScrollContainer, UIStage, UIImage, stage */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"UIManager\", function() { return UIManager; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"CowContainer\", function() { return CowContainer; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"ColItem\", function() { return ColItem; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"ScrollContainer\", function() { return ScrollContainer; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"UIStage\", function() { return UIStage; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"UIImage\", function() { return UIImage; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"stage\", function() { return stage; });\nvar __extends = (undefined && undefined.__extends) || (function () {\r\n    var extendStatics = function (d, b) {\r\n        extendStatics = Object.setPrototypeOf ||\r\n            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||\r\n            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };\r\n        return extendStatics(d, b);\r\n    };\r\n    return function (d, b) {\r\n        extendStatics(d, b);\r\n        function __() { this.constructor = d; }\r\n        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());\r\n    };\r\n})();\r\nvar Handler = /** @class */ (function () {\r\n    function Handler(thisObj, callback, once) {\r\n        this._id = \"event_\" + Handler.IDIndex++;\r\n        this.thisObj = thisObj;\r\n        this.callback = callback;\r\n        this.once = once;\r\n    }\r\n    Object.defineProperty(Handler.prototype, \"id\", {\r\n        get: function () { return this._id; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Handler.prototype.execute = function () {\r\n        var args = [];\r\n        for (var _i = 0; _i < arguments.length; _i++) {\r\n            args[_i] = arguments[_i];\r\n        }\r\n        if (this.callback) {\r\n            var r = this.callback.apply(this.thisObj, args);\r\n            if (this.once) {\r\n                this.callback = null;\r\n                return false;\r\n            }\r\n            return r;\r\n        }\r\n        return false;\r\n    };\r\n    Handler.IDIndex = 0;\r\n    return Handler;\r\n}());\r\nvar UIManager = /** @class */ (function () {\r\n    function UIManager() {\r\n    }\r\n    UIManager._OnResize = function (ev) {\r\n        console.log(\"Size : \" + document.documentElement.clientWidth + \",\" + document.documentElement.clientHeight);\r\n        stage.render();\r\n    };\r\n    UIManager.Init = function () {\r\n        //禁用滚动条\r\n        document.body.style.overflow = \"hidden\";\r\n        document.body.style.position = \"absolute\";\r\n        document.body.style.width = \"100%\";\r\n        document.body.style.height = \"100%\";\r\n        //添加Resize事件\r\n        document.body.onresize = UIManager._OnResize;\r\n        //初始化的时候先执行一下Resize\r\n        UIManager._OnResize.call(null, null);\r\n        UIBus.Init();\r\n    };\r\n    return UIManager;\r\n}());\r\n\r\nvar UIBus = /** @class */ (function () {\r\n    function UIBus() {\r\n    }\r\n    UIBus.Init = function () {\r\n        setInterval(UIBus.OnBeat, 1000 / 30);\r\n    };\r\n    UIBus.OnBeat = function () {\r\n        if (UIBus._queue.length > 0) {\r\n            var list = UIBus._queue;\r\n            UIBus._queue = [];\r\n            for (var i = 0; i < list.length; ++i) {\r\n                if (list[i]) {\r\n                    list[i].render();\r\n                }\r\n            }\r\n        }\r\n    };\r\n    UIBus.addRenderNode = function (node) {\r\n        if (!node)\r\n            return;\r\n        for (var i = 0; i < this._queue.length; ++i) {\r\n            if (this._queue && this._queue[i].id == node.id)\r\n                return;\r\n        }\r\n        this._queue.push(node);\r\n    };\r\n    //等待刷新的队列\r\n    UIBus._queue = [];\r\n    return UIBus;\r\n}());\r\nvar UINode = /** @class */ (function () {\r\n    function UINode() {\r\n        this._childNodes = [];\r\n        this._id = \"ui_\" + UINode.IDIndex++;\r\n        //创建的时候就认为要渲染\r\n        UIBus.addRenderNode(this);\r\n    }\r\n    Object.defineProperty(UINode.prototype, \"id\", {\r\n        get: function () { return this._id; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UINode.prototype, \"width\", {\r\n        get: function () { return 0; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UINode.prototype, \"height\", {\r\n        get: function () { return 0; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UINode.prototype, \"parent\", {\r\n        get: function () { return this._parent; },\r\n        set: function (node) { this._parent = node; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    UINode.prototype.render = function () {\r\n        //子节点也要刷新一下\r\n        this._childNodes.forEach(function (it) {\r\n            UIBus.addRenderNode(it);\r\n        });\r\n    };\r\n    UINode.prototype.refresh = function () {\r\n        UIBus.addRenderNode(this);\r\n    };\r\n    UINode.prototype.addChild = function (node) {\r\n        node.parent = this;\r\n        this._childNodes.push(node);\r\n    };\r\n    UINode.IDIndex = 0;\r\n    return UINode;\r\n}());\r\nvar UIElement = /** @class */ (function (_super) {\r\n    __extends(UIElement, _super);\r\n    function UIElement(element) {\r\n        var _this = _super.call(this) || this;\r\n        _this._element = null;\r\n        _this._x = 0;\r\n        _this._y = 0;\r\n        _this._width = 0;\r\n        _this._height = 0;\r\n        _this._element = element;\r\n        return _this;\r\n    }\r\n    Object.defineProperty(UIElement.prototype, \"element\", {\r\n        get: function () { return this._element; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UIElement.prototype, \"x\", {\r\n        get: function () { return this._x; },\r\n        set: function (v) {\r\n            this._x = v;\r\n            if (this._element)\r\n                this._element.style.left = v.toString() + \"px\";\r\n        },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UIElement.prototype, \"y\", {\r\n        get: function () { return this._y; },\r\n        set: function (v) {\r\n            this._y = v;\r\n            if (this._element)\r\n                this._element.style.top = v.toString() + \"px\";\r\n        },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UIElement.prototype, \"width\", {\r\n        get: function () { return this._width; },\r\n        set: function (v) {\r\n            this._width = v;\r\n            if (this._element)\r\n                this._element.style.width = v + \"px\";\r\n        },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UIElement.prototype, \"height\", {\r\n        get: function () { return this._height; },\r\n        set: function (v) {\r\n            this._height = v;\r\n            if (this._element)\r\n                this._element.style.height = v + \"px\";\r\n        },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    return UIElement;\r\n}(UINode));\r\nvar CowContainer = /** @class */ (function (_super) {\r\n    __extends(CowContainer, _super);\r\n    function CowContainer() {\r\n        var args = [];\r\n        for (var _i = 0; _i < arguments.length; _i++) {\r\n            args[_i] = arguments[_i];\r\n        }\r\n        var _this = _super.call(this) || this;\r\n        _this.cols = [];\r\n        _this._eles = [];\r\n        if (args) {\r\n            var self_1 = _this;\r\n            args.forEach(function (it) {\r\n                self_1.cols.push(it);\r\n            });\r\n        }\r\n        return _this;\r\n    }\r\n    CowContainer.prototype.render = function () {\r\n        var x = 0;\r\n        for (var i = 0; i < this.cols.length; ++i) {\r\n            if (!this._eles[i]) {\r\n                var ele = new ColItem();\r\n                this._eles[i] = ele;\r\n                this.parent.element.appendChild(this._eles[i].element);\r\n            }\r\n            var width = Math.round(this.parent.width * this.cols[i] / 100);\r\n            this._eles[i].width = width;\r\n            this._eles[i].x = x;\r\n            this._eles[i].height = Math.round(this.parent.height);\r\n            x += width;\r\n        }\r\n        _super.prototype.render.call(this);\r\n    };\r\n    return CowContainer;\r\n}(UINode));\r\n\r\nvar ColItem = /** @class */ (function (_super) {\r\n    __extends(ColItem, _super);\r\n    function ColItem() {\r\n        var _this = _super.call(this, document.createElement(\"div\")) || this;\r\n        _this.element.style.position = \"absolute\";\r\n        return _this;\r\n    }\r\n    ColItem.prototype.render = function () {\r\n    };\r\n    return ColItem;\r\n}(UIElement));\r\n\r\nvar ScrollContainer = /** @class */ (function (_super) {\r\n    __extends(ScrollContainer, _super);\r\n    function ScrollContainer() {\r\n        return _super !== null && _super.apply(this, arguments) || this;\r\n    }\r\n    return ScrollContainer;\r\n}(UINode));\r\n\r\n/**\r\n * 舞台\r\n */\r\nvar UIStage = /** @class */ (function (_super) {\r\n    __extends(UIStage, _super);\r\n    function UIStage() {\r\n        var _this = _super.call(this, null) || this;\r\n        _this._element = document.documentElement;\r\n        return _this;\r\n    }\r\n    Object.defineProperty(UIStage.prototype, \"width\", {\r\n        get: function () { return document.documentElement.clientWidth; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    Object.defineProperty(UIStage.prototype, \"height\", {\r\n        get: function () { return document.documentElement.clientHeight; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    return UIStage;\r\n}(UIElement));\r\n\r\nvar UIImage = /** @class */ (function (_super) {\r\n    __extends(UIImage, _super);\r\n    function UIImage(src) {\r\n        var _this = _super.call(this, new Image) || this;\r\n        _this.img.src = src;\r\n        _this.img.style.position = \"absolute\";\r\n        _this.img.style.top = \"0\";\r\n        _this.img.style.left = \"0\";\r\n        document.body.appendChild(_this.img);\r\n        return _this;\r\n    }\r\n    Object.defineProperty(UIImage.prototype, \"img\", {\r\n        get: function () { return this._element; },\r\n        enumerable: true,\r\n        configurable: true\r\n    });\r\n    return UIImage;\r\n}(UIElement));\r\n\r\nvar stage = new UIStage();\r\n//__________________________________________________________________________________________\r\nfunction test() {\r\n    var handler = new Handler(null, test2);\r\n}\r\nfunction test2() {\r\n}\r\ntest();\r\n\n\n//# sourceURL=webpack:///./web/src/main/webapp/js/src/tsui.ts?");

/***/ })

/******/ });