import jQuery from 'jquery';
import * as tsui from './tsui'

jQuery(document).ready((el) =>
{
	tsui.UIManager.Init();

	let cols = new tsui.CowContainer(30, 70);
	tsui.stage.addChild(cols);
});

