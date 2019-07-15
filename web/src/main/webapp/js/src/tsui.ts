import jq from 'jquery';

class Handler
{
	private static IDIndex = 0;

	private _id: string;
	thisObj: any;
	callback: Function
	once: boolean;

	public get id(): string { return this._id; }

	public constructor(thisObj: any, callback: Function, once?: boolean)
	{
		this._id = "event_" + Handler.IDIndex++;
		this.thisObj = thisObj;
		this.callback = callback;
		this.once = once;
	}

	public execute(...args: any[]): boolean
	{
		if (this.callback)
		{
			let r = this.callback.apply(this.thisObj, args);
			if (this.once)
			{
				this.callback = null;
				return false;
			}

			return r;
		}

		return false;
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
	protected _width: number = 0;
	protected _height: number = 0;

	public get id(): string { return this._id; }
	public get parent(): UINode { return this._parent; }
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

		//创建的时候就认为要渲染
		UIBus.addRenderNode(this);
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

class UIElement extends UINode
{
	protected _element: HTMLElement = null;

	constructor(element: HTMLElement)
	{
		super();
		this._element = element;
	}

	public get element(): HTMLElement { return this._element; }

	public render()
	{
		super.render();

		this._childNodes.forEach(it =>
		{
			if (it && it instanceof UIElement)
			{
				this._element.appendChild((it as UIElement).element);
			}
		});
	}
}

export class CowContainer extends UINode
{
	private _eles: ColItem[] = [];

	constructor(...args: number[])
	{
		super();

		if (args)
		{
			let self = this;
			args.forEach(it =>
			{
				self._eles.push(new ColItem(it));
			});
		}
	}

	public render()
	{
		let x = 0;
		for (let i = 0; i < this._eles.length; ++i)
		{
			let width = Math.round(this.parent.width * this._eles[i].widthVal / 100);
			this._eles[i].width = width;
			this._eles[i].x = x;
			this._eles[i].height = Math.round(this.parent.height);
			x += width;
			this._eles[i].render();
		}

		super.render();
	}

	public getItem(n: number): ColItem
	{
		return this._eles[n];
	}
}

export class ColItem extends UIElement
{
	public widthVal: number;

	constructor(widthVal: number)
	{
		super(document.createElement("div"));
		this.widthVal = widthVal;
		this.element.style.position = "absolute";
		this._element.id = this.id;
	}

	public render()
	{
		this._childNodes.forEach(it =>
		{
			if (it)
			{
				this._element.appendChild((it as UIElement).element);
			}
		});

		super.render();
	}
}

export class ScrollContainer extends UIElement
{
	constructor()
	{
		super(document.createElement("div"));
	}

	public render()
	{
		this.element.style.overflow = "scroll";
		this.width = this._parent.width;
		this.height = this._parent.height;
	}
}

/**
 * 舞台
 */
export class UIStage extends UIElement
{
	constructor()
	{
		super(null);
		this._element = document.documentElement;
	}

	public get width(): number { return document.documentElement.clientWidth; }
	public get height(): number { return document.documentElement.clientHeight; }
}

export class UIImage extends UIElement
{
	get img(): HTMLImageElement { return this._element as HTMLImageElement; }

	constructor(src: string)
	{
		super(new Image);
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