import jQuery from 'jquery';
import * as mirror from './mirror_ui';
jQuery(document).ready(function (el) {
    document.body.style.position = "absolute";
    document.body.style.width = "100%";
    document.body.style.height = "100%";
    console.log(document.documentElement.clientWidth);
    console.log(document.documentElement.clientHeight);
    var img = new mirror.UIImage("img/logo1.png");
    img.x = 100;
    img.y = 120;
    mirror.UIManager.Init();
});
