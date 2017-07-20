;
(function($) {
    'use strict'

    var agile = function(params) {
        var defaults = {
            mode: 'normal',
            type: 1,
            title: '',
            shadeClose: false,
            width: '0',
            height: '0',
            btn: ['确 定', '取 消'],
            callback: undefined,
            url: '',
            maxmin: false,
            max: false
        }
        params = $.extend(defaults, params);
        var url = params.url;
        if (!url.indexOf("/") === 0) {
            url = "/" + url;
        }
        var mode = params.mode;
        if(!mode){
     	   return;
        } 
        if(params.width && (params.width+"").indexOf("%") == -1){
     	   params.width = params.width + "px";
        }
        if(params.height && (params.height+"").indexOf("%") == -1){
     	   params.height = params.height + "px";
        }
        if ('add' === params.mode) {
            $.get(url, function(rs) {
                var layerDialog = layer.open({
                    type: params.type,
                    title: params.title,
                    shadeClose: params.shadeClose,
                    shade: 0.8,
                    area: [params.width, params.height],
                    content: rs,
                    maxmin: params.maxmin,
                    anim: 3,
                    btn: params.btn,
                    yes: function(index, layero) {
                       saveBtnClick(params.callback);
                    }
                });

                if (params.max) {
                    layer.full(layerDialog);
                }
            });
        }
    }
    $.agile = agile;
})(jQuery);