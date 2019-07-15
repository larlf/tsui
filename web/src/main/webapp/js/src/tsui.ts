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

	private _id: string;
	private _parent: UINode;
	private _childNodes: UINode[] = [];

	public get id(): string { return this._id; }
	public get width(): number { return 0; }
	public get height(): number { return 0; }
	public get parent(): UINode { return this._parent; }
	public set parent(node: UINode) { this._parent = node; }

	public constructor()
	{
		this._id = "ui_" + UINode.IDIndex++;

		//创建的时候就认为要渲染
		UIBus.addRenderNode(this);
	}

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
		node.parent = this;
		this._childNodes.push(node);
	}
}

class UIElement extends UINode
{
	protected _element: HTMLElement = null;

	private _x: number = 0;
	private _y: number = 0;
	private _width: number = 0;
	private _height: number = 0;

	constructor(element: HTMLElement)
	{
		super();
		this._element = element;
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

	public get width(): number { return this._width; }
	public set width(v: number)
	{
		this._width = v;
		if (this._element)
			this._element.style.width = v + "px";
	}

	public get height(): number { return this._height; }
	public set height(v: number)
	{
		this._height = v;
		if (this._element)
			this._element.style.height = v + "px";
	}
}

export class CowContainer extends UINode
{
	public cols: number[] = [];
	private _eles: ColItem[] = [];

	constructor(...args: number[])
	{
		super();

		if (args)
		{
			let self = this;
			args.forEach(it =>
			{
				self.cols.push(it);
			});
		}
	}

	public render()
	{
		let x = 0;
		for (let i = 0; i < this.cols.length; ++i)
		{
			if (!this._eles[i])
			{
				let ele = new ColItem();
				this._eles[i] = ele;
				(this.parent as UIElement).element.appendChild(this._eles[i].element);
			}

			let width = Math.round(this.parent.width * this.cols[i] / 100);
			this._eles[i].width = width;
			this._eles[i].x = x;
			this._eles[i].height = Math.round(this.parent.height);
			x += width;
		}

		super.render();
	}
}

export class ColItem extends UIElement
{
	constructor()
	{
		super(document.createElement("div"));
		this.element.style.position = "absolute";
	}

	public render()
	{

	}
}

export class ScrollContainer extends UINode
{

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