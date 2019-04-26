import jq from 'jquery';

class UIManager
{
	public static _OnResize(this: GlobalEventHandlers, ev: UIEvent)
	{
		console.log("Size : " + document.documentElement.clientWidth + "," + document.documentElement.clientHeight);
	}

	public static Init()
	{
		//禁用滚动条
		document.body.style.overflow = "hidden";

		//添加Resize事件
		document.body.onresize = UIManager._OnResize;

		//初始化的时候先执行一下Resize
		UIManager._OnResize.call(null, null);

		UIBus.Init();
	}
}

class UIBus
{
	public static Init()
	{
		setInterval(UIBus.OnBeat, 1000/30);
	}

	public static OnBeat()
	{
		
	}
}

class UIElement
{
	protected _element: HTMLElement = null;
	private _x: number = 0;
	private _y: number = 0;
	private _width: number = 0;
	private _height: number = 0;

	constructor()
	{
	}

	protected refresh():void
	{

	}

	public get element(): HTMLElement { return this._element; }

	public get x(): number { return this._x; }
	public set x(v: number) 
	{
		this._x = v;
		if (this._element)
			this._element.style.left = v.toString() + "px";
	}

	public get y(): number { return this._y; }
	public set y(v: number) 
	{
		this._y = v;
		if (this._element)
			this._element.style.top = v.toString() + "px";
	}
}

class UIImage extends UIElement
{
	

	constructor(src: string)
	{
		super();
		let element = new Image();
		element.src = src;
		element.style.position = "absolute";
		element.style.top = "0";
		element.style.left = "0";
		document.body.appendChild(element);
		this._element = element;
	}



	fun1()
	{
	}
}

export { UIManager, UIImage }