import jq from 'jquery';

/**
 * 所有对象的基类
 */
class Object
{
	private static _Index = 0;

	private _hashCode: number;
	public get hashCode(): number { return this._hashCode; }

	constructor()
	{
		this._hashCode = Object._Index++;
	}
}

/**
 * 对回调的封装
 */
class Handler extends Object
{
	thisObj: any;
	callback: Function
	once: boolean;

	public constructor(thisObj: any, callback: Function, once?: boolean)
	{
		super();
		this.thisObj = thisObj;
		this.callback = callback;
		this.once = once;
	}

	public execute(...args: any[])
	{
		if (this.callback)
		{
			this.callback.apply(this.thisObj, args);
		}
	}
}



export class UIManager
{
	public static _OnResize(this: GlobalEventHandlers, ev: UIEvent)
	{
		console.log("Size : " + document.documentElement.clientWidth + "," + document.documentElement.clientHeight);
		stage.render();
	}

	public static Init()
	{
		//禁用滚动条
		document.body.style.overflow = "hidden";
		document.body.style.position = "absolute";
		document.body.style.width = "100%";
		document.body.style.height = "100%";

		//添加Resize事件
		document.body.onresize = UIManager._OnResize;

		//初始化的时候先执行一下Resize
		UIManager._OnResize.call(null, null);

		UIBus.Init();
	}
}

class UIBus
{
	//等待刷新的队列
	private static _queue: UINode[] = [];

	public static Init()
	{
		setInterval(UIBus.OnBeat, 1000 / 30);
	}

	public static OnBeat()
	{
		if (UIBus._queue.length > 0)
		{
			let list = UIBus._queue;
			UIBus._queue = [];

			for (let i = 0; i < list.length; ++i)
			{
				if (list[i])
				{
					list[i].render();
				}
			}
		}
	}

	public static addRenderNode(node: UINode)
	{
		if (!node)
			return;

		for (let i = 0; i < this._queue.length; ++i)
		{
			if (this._queue && this._queue[i].id == node.id)
				return;
		}

		this._queue.push(node);
	}
}

class UINode
{
	private static IDIndex = 0;

	protected _id: string;
	protected _parent: UINode;
	protected _childNodes: UINode[] = [];
	protected _x: number = 0;
	protected _y: number = 0;
	protected _width: number = 100;
	protected _height: number = 100;
	protected _element: HTMLElement = null;

	public get id(): string { return this._id; }
	public get parent(): UINode { return this._parent; }

	public get element(): HTMLElement { return this._element; }
	public get parentElement(): HTMLElement { return this._parent ? this._parent.element : null; }
	public get x(): number { return this._x; }
	public set x(v: number) { this._x = v; }
	public get y(): number { return this._y; }
	public set y(v: number) { this._y = v; }
	public get width(): number { return this._width; }
	public set width(v: number) { this._width = v; }
	public get height(): number { return this._height; }
	public set height(v: number) { this._height = v; }

	public constructor()
	{
		this._id = "ui_" + UINode.IDIndex++;
	}

	protected setParent(node: UINode) { this._parent = node; }

	public render(): void
	{
		//子节点也要刷新一下
		this._childNodes.forEach(it =>
		{
			UIBus.addRenderNode(it);
		});
	}

	public refresh()
	{
		UIBus.addRenderNode(this);
	}

	public addChild(node: UINode)
	{
		node.setParent(this);
		this._childNodes.push(node);
	}
}

/**
 * 舞台
 */
export class UIStage extends UINode
{
	constructor()
	{
		super();
		this._element = document.documentElement;
		UIBus.addRenderNode(this);
	}

	public get width(): number { return document.documentElement.clientWidth; }
	public get height(): number { return document.documentElement.clientHeight; }
}

export class CowContainer extends UINode
{
	constructor(...args: number[])
	{
		super();

		if (args)
		{
			let self = this;
			args.forEach(it =>
			{
				self.addChild(new ColItem(it));
			});
		}
	}

	public render()
	{
		if (this._parent)
			this._element = this._parent.element;
		else
			this._element = null;

		let x = 0;
		for (let i = 0; i < this._childNodes.length; ++i)
		{
			let node: ColItem = this._childNodes[i] as ColItem;
			let width = Math.round(this.parent.width * node.widthVal / 100);
			this._childNodes[i].x = x;
			this._childNodes[i].width = width;
			this._childNodes[i].height = Math.round(this.parent.height);
			x += width;
		}

		super.render();
	}

	public getItem(n: number): ColItem
	{
		return this._childNodes[n] as ColItem;
	}
}

export class ColItem extends UINode
{
	public widthVal: number;

	constructor(widthVal: number)
	{
		super();
		this._element = document.createElement("div");
		this.widthVal = widthVal;
		this.element.style.position = "absolute";
		this._element.id = this.id;
	}

	public render()
	{
		if (this._parent)
		{
			this._parent.element.appendChild(this._element);
		}

		this._element.style.width = this._width + "px";
		this._element.style.height = this._height + "px";
		this._element.style.left = this._x + "px";
		this._element.style.top = this._y + "px";

		super.render();
	}
}

export class ScrollContainer extends UINode
{
	constructor()
	{
		super();
		this._element = document.createElement("div");
		this._element.id = this.id;
	}

	public render()
	{
		if (this._parent)
			this._parent.element.appendChild(this._element);

		this.element.style.overflow = "scroll";
		this._element.style.width = this._parent.width + "px";
		this._element.style.height = this._parent.height + "px";
		this._width = this._element.scrollWidth;
		this._height = this._element.scrollHeight;
		console.log(this.width, this.height);

		super.render();
	}
}

export class UIImage extends UINode
{
	get img(): HTMLImageElement { return this._element as HTMLImageElement; }

	constructor(src: string)
	{
		super();
		this._element = new Image;
		this.img.src = src;
		this.img.style.position = "absolute";
		this.img.style.top = "0";
		this.img.style.left = "0";
		document.body.appendChild(this.img);
	}
}

export let stage: UIStage = new UIStage();

//__________________________________________________________________________________________

function test()
{
	let handler: Handler = new Handler(null, test2);
}

function test2()
{

}

test();