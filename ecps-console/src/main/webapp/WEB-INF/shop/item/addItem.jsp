<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/ecps/console/common/taglibs.jsp"%> 
<head>
<title>添加商品_商品分类_实体商品_商品管理</title>
<meta name="heading" content="添加商品"/>
<meta name="menu" content="ItemMgmtMenu"/>
<script type="text/javascript" src="<c:url value='/${system }/res/plugins/fckeditor/fckeditor.js'/>"></script>
<script type="text/javascript" src="<c:url value='/${system }/res/js/jquery.form.js'/>"></script>
<script type="text/javascript" src="<c:url value='/${system }/res/js/uploads.js'/>"></script>
<script type="text/javascript"> var path = "${path}";</script>
<script type="text/javascript" src="${path }/ecps/console/res/item/js/addItem.js">
</script>
</head>
<body id="main">

<div class="frameL"><div class="menu icon">
	<jsp:include page="/${system }/common/itemmenu.jsp" />
</div></div>

<div class="frameR"><div class="content">

<div class="loc icon"><samp class="t12"></samp>当前位置:商品管理&nbsp;&raquo;&nbsp;<a href="<c:url value="/${system }/item/listEntity.do"/>" title="实体商品">实体商品</a>&nbsp;&raquo;&nbsp;<a href="<c:url value="/${system }/item/addCatItem.do"/>" title="商品分类">商品分类</a>&nbsp;&raquo;&nbsp;<span class="gray">添加商品</span><a href="<c:url value="/${system }/item/addCatItem.do"/>" title="返回商品分类" class="inb btn80x20">返回商品分类</a></div>
<form action="${path}/item/addItem.do" name="myForm" id="myForm" method="post">
<h2 class="h2_ch"><span id="tabs" class="l">
<a href="javascript:void(0);" ref="#tab_1" title="基本信息" class="here">基本信息</a>
<a href="javascript:void(0);" ref="#tab_2" title="商品描述" class="nor">商品描述</a>
<a href="javascript:void(0);" ref="#tab_3" title="商品参数" class="nor">商品参数</a>
<a href="javascript:void(0);" ref="#tab_4" title="商品规格" class="nor">商品规格</a>
</span></h2>
<div id="tab_1" class="edit set">
   
	<p><label><samp>*</samp>商品名称：</label>
	<input type="text" reg1="^(.{1,100})$" desc="100以内任意字符" id="itemName" name="itemName" class="text state" value="${ebItem.itemName}"  maxlength="100"/></p>
	<input type="hidden" id="catId" name="catId" value="1" />
	<p><label>商品品牌：</label>
	<select id="brandId" name="brandId">
		<option value="">请选择</option>
        <c:forEach items="${bList }" var="brand">
        	<option value="${brand.brandId }">${brand.brandName }</option>
        </c:forEach>
	</select></p>
	
	
	

	<div id="tagId" class="up_box" style="display:none">
	
		
		
	</div>
   	<p><label>促销语：</label>
   		<input type="text" id="promotion" name="promotion" reg1="^(.{0,100})$" desc="100以内任意字符" class="text state"  value="${ebItem.promotion}" maxlength="100"/>
   		<span class="pos"></span>
   	</p>
   <p><label>是否是新品：</label>
    	<input  name="isNew" type="radio" value="1" checked="checked"/>是&nbsp;&nbsp;
    	<input  name="isNew" type="radio" value="0" />否&nbsp;&nbsp;
    </p>
     <p><label>是否是推荐：</label>
    	<input  name="isGood" type="radio" value="1" />是&nbsp;&nbsp;
    	<input  name="isGood" type="radio" value="0" checked="checked"/>否&nbsp;&nbsp;
    </p>
    <p><label>是否是热卖：</label>
    	<input  name="isHot" type="radio" value="1" />是&nbsp;&nbsp;
    	<input  name="isHot" type="radio" value="0" checked="checked"/>否&nbsp;&nbsp;
    </p>
    <a name="uploadImgs" id="uploadImgs"></a>
    <p><label><samp>*</samp>上传商品图片(90x150尺寸)：</label><span id="uploadImgTip1" class="orange">注:该尺寸图片必须为90x150。</span></p>
    <p><label></label>
		<img id='imgsImgSrc' src='${path}/ecps/console/images/logo266x64.png'  height="100" width="100" />
		<input type='file' id='imgsFile' name='imgsFile' class="file" onchange='submitImgSize1Upload()' /><span class="pos" id="imgSize1FileSpan">请上传图片的大小不超过3MB</span>
        <input type='hidden' id='imgs' name='imgs' value='' reg="^.+$" tip="亲！您忘记上传图片了。" />
	</p>
	<p>
	
	<label>页面关键词：</label><input type="text" reg1="^.{0,50}$" desc="50个字符以内" id="keywords" name="keywords" class="text state" value="${ebItem.keywords}"/>
	</p>
	<p><label class="alg_t">页面描述：</label><textarea  id="pageDesc" reg1="^(.|\n){0,130}$" desc="130个以内的任意字符" name="pageDesc" class="are" rows="6" cols="45">${ebItem.pageDesc}</textarea>
	</p>
   
</div>

<div id="tab_2" class="edit" style="display: none">
<textarea name="itemDesc" id="itemDesc" ></textarea>

</div>

<div id="tab_3" class="edit set" style="display: none">
   <c:if test="${fn:length(commList) == 0 }">
   		<p><label>无属性</label></p>
   </c:if>
   <c:forEach items="${commList }" var="feature">
   	<p>
   		<label>${feature.featureName }:</label>
   		<c:if test="${feature.inputType == 1 }">
   			<select name="${feature.featureId }">
   				<option value="">请选择</option>
   				<c:forEach items="${feature.selectValues }" var="val">
   					<option value="${val }">${val }</option>
   				</c:forEach>
   			</select>
   		</c:if>
   		<c:if test="${feature.inputType == 2 }">
   			<c:forEach items="${feature.selectValues }" var="val">
   				<input type="radio" name="${feature.featureId }" value="${val }">${val }&nbsp;
   			</c:forEach>
   		</c:if>
   		<c:if test="${feature.inputType == 3 }">
   			<c:forEach items="${feature.selectValues }" var="val">
   				<input type="checkbox" name="${feature.featureId }" value="${val }">${val }&nbsp;
   			</c:forEach>
   		</c:if>
   	</p>
   </c:forEach>
    
	    
	    
</div>

<div id="tab_4" style="display:none">
	
	
    <div id="sp_0" class="sp_0">
        <table cellspacing="0" summary="" class="tab3">
            <c:if test="${fn:length(specList) == 0}">
            <tr><th colspan="2" class="gray b">&nbsp;&nbsp;<b>默认</b></th></tr>
            </c:if>
            <c:forEach items="${specList }" var="feature">
            	<tr>
            		<td>${feature.featureName }</td>
            		<td>
            			<c:forEach items="${feature.selectValues }" var="val">
            				<input type="radio" name="${feature.featureId }specradio1" value="${val }">${val }&nbsp;
            			</c:forEach>
            		</td>
            	</tr>
            </c:forEach>
           
            <tr><td colspan="2">
                 <table cellspacing="0">
                    <tr>
                        <th>排序</th>
                        <th>商城价</th>
                        <th>市场价</th>
                        <th>库存</th>
                        <th>购买上限</th>
                        <th>货号</th>
                        <th style='display:none;' >货位</th>
                        <th>上架</th>
                        <th>类型</th>
                        <th>操作</th>
                    </tr>
                    <tr>                                                 
                        <td width="10%" class="nwp"><input type="text" reg1="^[0-9]{0,2}$" desc="2个字符以内" id="sort" class="text20" name="sort1" maxlength="2"  size="5" /></td>
                        <td width="12%" class="nwp"><samp class="red">*</samp> <input reg1="^[0-9]{1,7}\.{0,1}[0-9]{0,2}$" desc="保留2位小数，最多允许9位有效数字" type="text" id="skuPrice" name="skuPrice1"class="text20" size="5" onblur="changePri(this)"/></td>
                        <td width="12%" class="nwp"><input type="text" id="marketPrice" name="marketPrice1" class="text20" reg1="^[0-9]{0,7}\.{0,1}[0-9]{0,2}$" desc="保留2位小数，最多允许9位有效数字" size="5" onblur="changePri(this)"/></td>
                        <td width="12%" class="nwp"><samp class="red">*</samp><input reg1="^(0|[1-9][0-9]{0,4})$" desc="5个字符以内非负整数" type="text" id="stockInventory" name="stockInventory1" class="text20" size="5" /></td>
                        <td width="12%" class="nwp"><input reg1="^(.{0,0}|0|[1-9][0-9]{0,4})$" desc="请输入5个字符以内非负整数或为空 " type="text" id="skuUpperLimit" name="skuUpperLimit1" class="text20" size="5"/></td>
                        <td width="12%"  class="nwp"><input type="text" id="sku" name="sku1" class="text20" reg1="^[a-zA-Z0-9_\u4e00-\u9fa5]{0,20}$" desc="20个字符以内" size="5"/></td>
                        <td width="12%" class="nwp" style='display:none;' ><input reg1="^[a-zA-Z0-9_\u4e00-\u9fa5]{0,20}$" desc="20个字符以内" type="text" id="location" name="location1" class="text20" size="5" /></td>
                        <td>
                        <select id="showStatus1" name="showStatus1">
                        <option value="0" selected>上架 </option>
                        <option value="1" >下架</option>
                        </select>
                        </td>
                        <td>
                         <select id="skuType" name="skuType1">
                        	<!--  option value="0">赠品</option-->
                        	<option value="1" selected>普通</option>
                        </select>
                        </td>
                        <td><input type="button" value="删除" class="hand btn60x20" onclick="clickRemove('#sp_0')"/></td>
                    </tr>
                </table>
            </td></tr>
        </table>

    </div>
    
    <c:if test="${fn:length(specList) != 0}">
        <div class="page_c">
        	<span class="r"><input type="button" value="增加规格" id="button2" name="button2" class="hand btn80x20" />
        	</span>
        </div>
    </c:if>
</div>


<div class="loc">&nbsp;</div>

<div class="edit set">
<p><label>&nbsp;</label><input id="button1"
	type="submit" value="提 交" class="hand btn83x23" />&nbsp;&nbsp; <input
	type="button" onclick="javascript:;history.back();" value="取消" class="hand btn83x23b" /></p>
</div>
<input  type="hidden" id="divNum" name="divNum" value="1"/>
</form>
<div class="loc">&nbsp;</div>

</div></div>
</body>

