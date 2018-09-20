$(function(){
  $('#buyopts dl.ck a').on('click', function(){
    $(this).siblings('a.cur').removeClass('cur');
    $(this).addClass('cur').closest('dl.ck').data('checked', $(this).data('key'));
    var spec = [0, 0, 0];
    $("dd a.cur").each(function(i, o){
        spec[i] = $(o).data("id");
    });
    console.info(spec);
    var cur_gs = null;
    for(var i=0; i<goodsSpecs.length; i++) {
        var gs = goodsSpecs[i];
        if (gs.property1 == spec[0] && gs.property2 == spec[1] && gs.property3 == spec[2]) {
            cur_gs = gs;
            break;
        }
    }
    var itemUrl = null;
    if (cur_gs == null) {
        itemUrl = "/mshop/item/show/" + shopId + "/" + goodsId + ".shtml";
    } else {
        itemUrl = "/mshop/item/show/" + shopId + "/" + goodsId + "-" + cur_gs.id + ".shtml";
    }
    window.location = itemUrl;
  });
});

function albumSlider(){
  var container = $('#gims'), slider = container.find('.slider'), _item = slider.find('a'), totals = slider.find('a').length;
  var sx, mx = 0;
  var iw = _item.width();

  container.find('.trigger font').text(totals);
  
  _item.on('touchstart', function(e){sx =  e.touches[0].pageX;});
  _item.on('touchmove', function(e){mx = e.touches[0].pageX - sx;});
  _item.on('touchend', function(){
    if(Math.abs(mx) > 40){
      var i = parseInt(container.find('.trigger b').text()), tx;
      if(mx < 0){
        if(i == totals){
          tx = 0;
          container.find('.trigger b').text(1);
        }else{
          tx = 0 - (iw * i);
          container.find('.trigger b').text(i + 1);
        }
      }else{
        if(i == 1){
          tx = 0 - iw * (totals - 1);
          container.find('.trigger b').text(totals);
        }else{
          tx = 0 - (iw * (i - 2));
          container.find('.trigger b').text(i - 1);
        }
      }
      slider.animate({left: tx}, 200);
      mx = 0;
    }
  });
}

function addToCart(id){
  var optsGroup = $('#buyopts dl.ck'), key = id, optids = [], verify = true;
  if(optsGroup.length > 0){
    optsGroup.each(function(i, e){
      if(!$(e).data('checked') || $(e).data('checked') == ''){
        $.vdsPrompt({content:'请选择'+$(e).find('dt').data('name')});
          verify = false;
          return false;
      }
      optids[i] = $(e).data('checked');
      key += '_'+$(e).data('checked');
    });
  }
  if(verify){
    var count = $("#buy-qty").val();
    flyToCartAnimate();
    incrCartNum();

    $.asynInter("/mall/cart/add/1.api", {id:id, count:count}, function(res){});
  }
}

function toBuy(id, target){
    var optsGroup = $('#buyopts dl.ck'), key = id, optids = [], verify = true;
    if(optsGroup.length > 0){
        optsGroup.each(function(i, e){
            if(!$(e).data('checked') || $(e).data('checked') == ''){
                $.vdsPrompt({content:'请选择'+$(e).find('dt').data('name')});
                verify = false;
                return false;
            }
            optids[i] = $(e).data('checked');
            key += '_'+$(e).data('checked');
        });
    }
    if(verify){
        var count = $("#buy-qty").val();
        flyToCartAnimate();
        incrCartNum();

        $.asynInter("/mall/cart/add/1.api", {id:id, count:count}, function(res){
            window.location.href = target;
        });
    }
}

function changeBuyQty(act){
  var qty = $('input#buy-qty'), qty_val = parseInt(qty.val());
  if(act == 'incr'){
    var stock = qty.data('stock');
    if(qty.val() < stock){
      qty.val(qty_val + 1);
    }else{
      $.vdsPrompt({content:'此商品最多只能购买'+stock+'件'});
    }
  }else{
    if(qty_val > 1){
      qty.val(qty_val - 1);
    }else{
      $.vdsPrompt({content:'购买数量不能少于1件'});
    }
  }
}

function incrCartNum(){
  var countBar = $('#cartbar em');
  countBar.css('display', 'block').text(parseInt(countBar.text()) + 1);
}

function flyToCartAnimate(){
  var circle;
  var locate = $('#addcartbtn');
  var dest = $('#cartbar');
  if($('#vds-fly-circle').length > 0){
    circle = $('#vds-fly-circle');
  }else{
    circle = $('<i></i>', {id:'vds-fly-circle'}).appendTo($('body'));
  }
  
  circle.css({
    display: 'block',
    position: 'absolute',
    width: '8px',
    height: '8px',
    'background-color': '#ff6666',
    'border-radius': '15px',
    top: locate.offset().top,
    left: locate.offset().left + (locate.width() / 2 - 4),
    'z-index': 99999,
  });
    
  var flyH = $(window).height() / 10 < 50 ? 50 : $(window).height() / 10, flyM = Math.abs((locate.offset().left - dest.offset().left) / 2);
  circle.animate({
    width: '16px',
    height: '16px',
    top: circle.offset().top - flyH,
    left: circle.offset().left - flyM,
    //translate3d: '-'+ (locate.offset().left - dest.offset().left) / 2 + 'px, -50px , 0', 
  }, 200, 'linear', function(){
    circle.animate({
      width: '6px',
      height: '6px',
      top: dest.offset().top + 5,
      left: dest.offset().left + (dest.width() / 2 - 3),
    }, 200, 'linear', function(){circle.hide()});
  });
}