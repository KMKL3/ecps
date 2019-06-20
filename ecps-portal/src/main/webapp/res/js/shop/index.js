$(function(){
	
	

	$("#loginAlertIs").click(function(){
		window.location.href = path+"/user/toLogin.do";
	});

	$("#promptAlertIs").click(function(){
		tipShow('#promptAlert');
	});

	$("#transitAlertIs").click(function(){
		tipShow('#transitAlert');
	});

	/*filter condition*/
	$('.btn80x22').toggle(
        function () {
            $(this).attr('title','收缩↑');
            $(this).attr('class','btn80x22 close');
            var len = $('.filter li').length;
            $('.filter li').slice(2,len-2).show();
        },
        function () {
            $(this).attr('title','展开↓');
            $(this).attr('class','btn80x22 open');
            var len = $('.filter li').length;
            $('.filter li').slice(2,len-2).hide();
        }
    );

	$('.filter li a').mousedown(function(){
    	var cln = $(this).attr("class");
		if(cln == undefined){
			$(this).attr("class",'');
			cln = $(this).attr("class");
		};
		if(cln.indexOf("btn80x22") == 0){
			return;
		}
		var type = $(this).parent().is("p");
		var obj;
		if(type){
			obj = $(this).parent().find('a');
		}else{
			obj = $(this).parent().parent().find('a');
		}
		obj.removeClass('here');
		$(this).addClass('here');
		
		var price = "";
		var brandId = "";
		var keyWords = $("#keyWords").val();
		var paraVals = "";
		//循环a链接找到被选中的a
		$('.filter li a').each(function(){
			var clazz = $(this).attr("class");
			if(clazz == "here"){
				//获得a的类型
				var fType = $(this).attr("fType");
				var fValue = $(this).attr("fValue");
				if(fType == "price"){
					price = fValue;
				}else if(fType == "brand"){
					brandId = fValue;
				}else if(fType == "feature" && fValue != ""){
					paraVals = paraVals + fValue +",";
				}
			}
		})
		//alert(price +"======="+brandId+ "========"+keyWords+"======="+paraVals);
		var iPath = path+"/item/listItem.do?price="+price+"&brandId="+brandId+"&keyWords="+keyWords+"&paraVals="+paraVals;
		//刷新iframe，很简单只要重置src即可
		$("#itemListIframe").attr("src", iPath);
    });

	$("#filterRest").click(function(){
		$('.filter li a').each(function(){
			$(this).removeClass('here');
		});
		$('.filter li').each(function(){
			$(this).find('a').eq(0).addClass('here');
		});
	});

	/* sales sales_down sales_up price price_down price_up time time_down time_up */
	$('.sort_type a').each(function(i, obj){
		$(obj).click(function(){
			var name = $(this).attr('class');
			$('.sort_type a').each(function(j, li){
				var tname = $(li).attr('class');
				if(j == 0&&tname!='sales'){$(li).attr('class','sales');}
				if(j == 1&&tname!='price'){$(li).attr('class','price');}
				if(j == 2&&tname!='time'){$(li).attr('class','time');}
			});
			if(name.indexOf('sales')>=0){
				if(name == 'sales'||name == 'sales_up'){
					$(this).attr('class','sales_down');
					$(this).attr('title','销量从高到低排序');
				}
				if(name == 'sales_down'){
					$(this).attr('class','sales_up');
					$(this).attr('title','销量从低到高排序');
				}
			};
			if(name.indexOf('price')>=0){
				if(name == 'price'||name == 'price_up'){
					$(this).attr('class','price_down');
					$(this).attr('title','点击按价格从高到低排序');
				}
				if(name == 'price_down'){
					$(this).attr('class','price_up');
					$(this).attr('title','点击按价格从低到高排序');
				}
			};
			if(name.indexOf('time')>=0){
				if(name == 'time'||name == 'time_up'){
					$(this).attr('class','time_down');
					$(this).attr('title','上架时间从近到远排序');
				}
				if(name == 'time_down'){
					$(this).attr('class','time_up');
					$(this).attr('title','上架时间从远到近排序');
				}
			};
		});
	});
	
});