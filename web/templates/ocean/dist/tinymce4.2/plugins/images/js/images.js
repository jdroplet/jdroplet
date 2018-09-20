$(function(){
	var getWin = function() {
		return (!window.frameElement && window.dialogArguments) || opener || parent || top;
	};
	var siteId = 1;

	//$("#site_id").val(siteId);
	//ЗАГРУЗКА
	$('#loader').show();
	//Строка адреса
	$.ajax({
		type: "POST",
		url: "connector/jsp/showpath.jsp",
		data: "type=images&path=&default=1&siteId=" + siteId,
		success: function(data){
			$('#addr').html(data);
		}
	});
	//目录文件夹
	$.ajax({
		type: "POST",
		url: "connector/jsp/showtree.jsp",
		data: "default=1",
		success: function(data){
			$('#tree').html(data);
		}
	});
	//文件列表
	$.ajax({
		type: "POST",
		url: "connector/jsp/showdir.jsp",
		data: "pathtype=images&path=&default=&siteId=" + siteId,
		success: function(data){
			$('#loader').hide();
			//$('#files').html(data);
			$('#mainFiles').html('<div id="files">'+data+'</div>');
			showFootInfo();
		}
	});
    //会话ID为Flash下载器
	var SID;
	$.ajax({
		type: "POST",
		url: "connector/jsp/sid.jsp",
		data: "action=SID",
		success: function(data){
			SID = data;
		}
	});
	
	
	//Адресная строка
	$('#addr').on('mouseover', '.addrItem div,.addrItem img', function(){
		$(this).parent().animate({backgroundColor:'#b1d3fa'}, 100, 'swing', function(){
			
		});
	});
	$('#addr').on('mouseout', '.addrItem div,.addrItem img',function(){
		$(this).parent().animate({backgroundColor:'#e4eaf1'}, 200, 'linear', function(){
			//alert('ck');
			$(this).css({'background-color':'transparent'});
			//alert('ck');
		});
	});
	$('#addr').on('mousedown', '.addrItem div,.addrItem img', function(){
		$(this).parent().css({'background-color':'#679ad3'});
	});

	$('#addr').on('mouseup', '.addrItem div,.addrItem img', function(){
		$(this).parent().css({'background-color':'#b1d3fa'});
		$.ajax({
			type: "POST",
			url: "connector/jsp/showtree.jsp",
			data: "path="+$(this).parent().attr('path')+"&type="+$(this).parent().attr('pathtype') + "&siteId=" + siteId,
			success: function(data){
				//$('#loader').hide();
				$('#tree').html(data);
			}
		});
		$.ajax({
			type: "POST",
			url: "connector/jsp/showpath.jsp",
			data: "type="+$(this).parent().attr('pathtype')+"&path="+$(this).parent().attr('path') + "&siteId=" + siteId,
			success: function(data){
				$('#addr').html(data);
			}
		});
		$.ajax({
			type: "POST",
			url: "connector/jsp/showdir.jsp",
			data: "pathtype="+$(this).parent().attr('pathtype')+"&path="+$(this).parent().attr('path') + "&siteId=" + siteId,
			success: function(data){
				$('#loader').hide();
				//$('#files').html(data);
				$('#mainFiles').html('<div id="files">'+data+'</div>');
				showFootInfo();
			}
		});
	});
	
	//Кнопка "В начало"
	$('#toBeginBtn').mouseover(function(){
		$(this).children(0).attr('src','img/backActive.gif');
	});
	$('#toBeginBtn').mouseout(function(){
		$(this).children(0).attr('src','img/backEnabled.gif');
	});
	
	//Меню
	$('#tree').on('mouseover', '.folderClosed,.folderOpened,.folderS,.folderImages,.folderFiles', function(){
		if(!$(this).hasClass('folderAct')) {
			$(this).addClass('folderHover');
		} else {
			$(this).addClass('folderActHover');
		}
	});
	$('#tree').on('mouseout','.folderClosed,.folderOpened,.folderS,.folderImages,.folderFiles',function(){
		if(!$(this).hasClass('folderAct')) {
			$(this).removeClass('folderHover');
		} else {
			$(this).removeClass('folderActHover');
		}
	});
	
	//Флаг загрузки
	var folderLoadFlag = false;
	//Открыть указанную папку
	function openFolder(type, path, callback) {
		$.ajax({
			type: "POST",
			url: "connector/jsp/showpath.jsp",
			data: "type="+type+"&path="+path + "&siteId=" + siteId,
			success: function(data){
				$('#addr').html(data);
			}
		});
		$.ajax({
			type: "POST",
			url: "connector/jsp/showdir.jsp",
			data: "pathtype="+type+"&path="+path + "&siteId=" + siteId,
			success: function(data){
				$('#loader').hide();
				//$('#files').html(data);
				$('#mainFiles').html('<div id="files">'+data+'</div>');
				showFootInfo();
				callback();
			}
		});
	}
	$('#tree').on('click', '.folderClosed,.folderOpened,.folderS,.folderImages,.folderFiles', function(){
		
		//Запрет на переключение
		if(folderLoadFlag) return false;
		folderLoadFlag = true;
		
		$('#loader').show();
		$('.folderAct').removeClass('folderAct');
		$(this).removeClass('folderHover');
		$(this).addClass('folderAct');
			
		openFolder($(this).attr('pathtype'), $(this).attr('path'), function(){ folderLoadFlag = false; });
	});

	$('#tree').on('dblclick', '.folderImages,.folderFiles', function(){
		$(this).next().slideToggle('normal');
	});

	$('#tree').on('dblclick', '.folderOpened,.folderS', function(){
		if(!$(this).next().hasClass('folderOpenSection')) return false;
		if($(this).hasClass('folderS')) {
			$(this).removeClass('folderS').addClass('folderOpened');
		} else {
			$(this).removeClass('folderOpened').addClass('folderS');
		}
		$(this).next().slideToggle('normal');
	});
	
	//ДЕЙСТВИЯ МЕНЮ
	//Открыть загрузчик файлов
	$('#menuUploadFiles').click(function(){
		var path = getCurrentPath();
		var str = '';
		if(path.type=='images') {
			str = '<span>Images:</span>';
		} else if(path.type=='files') {
			str = '<span>Files:</span>';
		}
		str += path.path;
		$('#uploadTarget').html(str);
		
		$('#normalPathVal').val(path.path);
		$('#normalPathtypeVal').val(path.type);
		
		$('#upload').show();
	});
	//Создать папку
	var canCancelFolder = true;
	$('#menuCreateFolder').click(function(){
		$(this).hide();
		$('#menuCancelFolder,#menuSaveFolder').show();
		
		$('.folderAct').after('<div id="newFolderBlock"><input type="text" name="newfolder" id="newFolder" /></div>');
		$('#newFolderBlock').slideDown('fast', function(){
			$('#newFolderBlock input').focus().blur(cancelNewFolder).keypress(function(e){
				if(e.which == 13) {
					saveNewFolder();
				} else if (e.which == 27) {
					cancelNewFolder();
				} else if ((e.which >= 97 && e.which <= 122) || (e.which >= 65 && e.which <= 90) || (e.which >= 48 && e.which <= 57) || e.which == 8 || e.which == 95 || e.which == 45 || e.keyCode == 37 || e.keyCode == 39 || e.keyCode == 16) {
					//Значит все верно: a-Z0-9-_ и управление вводом
				} else {
					return false;
				}
				
			});
		});
		
	});
	//Отменить создание папки
	function cancelNewFolder(){
		if(!canCancelFolder) {
			canCancelFolder = true;
			return false;
		}
		$('#menuCancelFolder,#menuSaveFolder').hide();
		$('#menuCreateFolder').show();
		
		$('#newFolderBlock').slideUp('fast', function(){
			$(this).remove();
		});
	}
	$('#menuCancelFolder').click(cancelNewFolder);
	
	//Подтвердить создание папки
	function saveNewFolder(){
		canCancelFolder = false;
		
		if($('#newFolderBlock input').val() == '') {
			alert('Enter a name for new folder');
			$('#newFolderBlock input').focus();
			return false;
		}
		
		$('#loader').show();
		$('#menuCancelFolder,#menuSaveFolder').hide();
		$('#menuCreateFolder').show();
		//Запрос на создание папки + сервер должен отдать новую структуру каталогов
		var pathtype = $('.folderAct').attr('pathtype');
		var path = $('.folderAct').attr('path');
		var path_new = $('#newFolderBlock input').val();
		var path_will = path+'/'+path_new;
		$.ajax({
			type: "POST",
			url: "connector/php/",
			data: "action=newfolder&type="+ pathtype +"&path="+ path +"&name=" + path_new,
			success: function(data){
				$('#loader').hide();
				var blocks = eval('('+data+')');
				if(blocks.error != '') {
					alert(blocks.error);
					$('#newFolderBlock input').focus();
				} else {
					$('#tree').html(blocks.tree);
					$('#addr').html(blocks.addr);
					canCancelFolder = true;
					
					//Открываем созданную папку
					$.ajax({
						type: "POST",
						url: "connector/jsp/showdir.jsp",
						data: "pathtype="+pathtype+"&path="+$('.folderAct').attr('path'),
						success: function(data){
							$('#loader').hide();
							//$('#files').html(data);
							$('#mainFiles').html('<div id="files">'+data+'</div>');
						}
					});
				}
			}
		});
	}
	$('#menuSaveFolder').click(saveNewFolder).hover(function(){ canCancelFolder = false; }, function(){ canCancelFolder = true; });
	
	//Удалить папку
	$('#menuDelFolder').click(function() {
		var path = getCurrentPath();
		if(confirm('Delete folder '+path.path+'?')) {
			$('#loader').show();
			$.ajax({
				type: "POST",
				url: "connector/php/",
				data: "action=delfolder&pathtype="+path.type+"&path="+path.path,
				success: function(data){
					var result = eval('('+data+')');
					if(typeof(result.error) != 'undefined') {
						$('#loader').hide();
						alert(result.error);
					} else {
						//$('#mainFiles').html('<div id="files">'+result.ok+'</div>');
						//showFootInfo();
						$.ajax({
							type: "POST",
							url: "connector/jsp/showtree.jsp",
							data: "action=showtree&path=&type="+path.type,
							success: function(data){
								//$('#loader').hide();
								$('#tree').html(data);
							}
						});
						openFolder(path.type, '', function(){ $('#loader').hide(); });
						
					}
					
				}
			});
		}
	});
	
	//Удалить файлы
	$('#menuDelFiles').click(function() {
		var files = $('.imageBlockAct');
		
		if(files.length == 0) {
			alert('Select files to delete.\n\nYou can select multiple files at once, so hold Ctrl while selecting.');
		} else if(files.length == 1) {
			if(confirm('Delete file '+files.attr('fname')+'.'+files.attr('ext')+'?')) {
				$('#loader').show();
				var path = getCurrentPath();
				$.ajax({
					type: "POST",
					url: "connector/php/",
					data: "action=delfile&pathtype="+path.type+"&path="+path.path+"&md5="+files.attr('md5')+"&filename="+files.attr('filename'),
					success: function(data){
						$('#loader').hide();
						//$('#files').html(data);
						if(data != 'error') {
							$('#mainFiles').html('<div id="files">'+data+'</div>');
							showFootInfo();
						} else {
							alert(data);
						}
					}
				});
			}
		} else {
			if(confirm('Files to delete: '+files.length+'\n\nContinue?')) {
				$('#loader').show();
				var path = getCurrentPath();
				
				//Собираем строку запроса
				var actionStr = 'action=delfile&pathtype='+path.type+'&path='+path.path;
				$.each(files, function(i, item){
					actionStr += "&md5["+i+"]="+$(this).attr('md5')+"&filename["+i+"]="+$(this).attr('filename');
				});
				
				$.ajax({
					type: "POST",
					url: "connector/php/",
					data: actionStr,
					success: function(data){
						$('#loader').hide();
						//$('#files').html(data);
						if(data != 'error') {
							$('#mainFiles').html('<div id="files">'+data+'</div>');
							showFootInfo();
						} else {
							alert(data);
						}
					}
				});
			}
		}
	});
	
	
	//Файлы
	var ctrlState = false;
	$('#mainField').on('mouseover', '.imageBlock0', function(){
		if(!$(this).hasClass('imageBlockAct')) {
			$(this).addClass('imageBlockHover');
		} else {
			$(this).addClass('imageBlockActHover');
		}
	});
	$('#mainField').on('mouseout', '.imageBlock0', function(){
		if(!$(this).hasClass('imageBlockAct')) {
			$(this).removeClass('imageBlockHover');
		} else {
			$(this).removeClass('imageBlockActHover');
		}
	});
	
	$('#insertImage').click(function(){
		$('.imageBlockAct').trigger('dblclick');
	});
	
	$('#mainField').on('dblclick', '.imageBlock0',function(){
		var e = $(this);
		
		if(e.attr('type') == 'files')
		{
			var filesize = e.attr('fsizetext');
			var text = '<a href="'+e.attr('linkto')+'" '+addAttr+' title="'+e.attr('fname')+'">';
			text += e.attr('fname');
			text += '</a> ' + ' ('+filesize+') ';
		}
		else
		{
			if(e.attr('fmiddle')) {
				var addAttr = (e.attr('fclass')!=''?'class="'+e.attr('fclass')+'"':'')+' '+(e.attr('frel')!=''?'rel="'+e.attr('frel')+'"':'');
				var text = '<a href="'+e.attr('linkto')+'" '+addAttr+' title="'+e.attr('fname')+'">';
				text += '<img src="'+e.attr('fmiddle')+'" width="'+e.attr('fmiddlewidth')+'" height="'+e.attr('fmiddleheight')+'" alt="'+e.attr('fname')+'" />';
				text += '</a> ';
			} else {
				var text = '<img src="'+e.attr('linkto')+'" width="'+e.attr('fwidth')+'" height="'+e.attr('fheight')+'" alt="'+e.attr('fname')+'" /> ';
			}
		}
		
		//ImagesDialog.insert(text);
		document.domain = 'tianya.cn';
		var win = getWin();
		var tinymce = win.tinymce;
		tinymce.EditorManager.activeEditor.insertContent(text);

		if($('.imageBlockAct').length == 1) {
			tinymce.EditorManager.activeEditor.windowManager.close(window);
		}
	});
	$('#mainField').on('click', '.imageBlock0', function(){
		if(ctrlState) {
			if($(this).hasClass('imageBlockActHover') || $(this).hasClass('imageBlockAct')) {
				$(this).removeClass('imageBlockAct');
				$(this).removeClass('imageBlockActHover');
			} else {
				$(this).removeClass('imageBlockHover');
				$(this).addClass('imageBlockAct');
			}
		} else {
			$('.imageBlockAct').removeClass('imageBlockAct');
			$(this).removeClass('imageBlockHover');
			$(this).addClass('imageBlockAct');
		}
		
		showFootInfo();
	});
	
	function selectAllFiles() {
		$('.imageBlock0').addClass('imageBlockAct');
		showFootInfo();
	}
	
	$(this).keydown(function(event){
		if(ctrlState && event.keyCode==65) selectAllFiles();
		if(event.keyCode==17) ctrlState = true;
	});
	$(this).keyup(function(event){
		if(event.keyCode==17) ctrlState = false;
	});
	$(this).blur(function(event){
		ctrlState = false;
	});
	
	
	
	//НИЖНЯЯ ПАНЕЛЬ
	//Показать текущую информацию
	function showFootInfo() {
		$('#fileNameEdit').show();
		$('#fileNameSave').hide();
		var file = $('.imageBlockAct');
		if(file.length > 1) {
			$('#footTableName, #footDateLabel, #footLinkLabel, #footDimLabel, #footDate, #footLink, #footDim').css('visibility','hidden');
			$('#footExt').text('Files selected: '+file.length);
			var tmpSizeCount = 0;
			$.each(file, function(i, item) {
				tmpSizeCount += parseInt($(this).attr('fsize'));
			});
			$('#footSize').text(intToMb(tmpSizeCount));
		} else if(file.length == 0) {
			$('#footTableName, #footDateLabel, #footLinkLabel, #footDimLabel, #footDate, #footLink, #footDim').css('visibility','hidden');
			var allFiles = $('.imageBlock0');

			$('#footExt').text('Files: '+allFiles.length);
			var tmpSizeCount = 0;
			$.each(allFiles, function(i, item) {
				tmpSizeCount += parseInt($(this).attr('fsize'));
			});
			$('#footSize').text(intToMb(tmpSizeCount));
		} else {
			
			$('#fileName').text(file.attr('fname'));
			$('#footExt').text(file.attr('ext'));
			$('#footDate').text(file.attr('date'));
			$('#footLink a').text(file.attr('fname').substr(0,16)).attr('href',file.attr('linkto'));
			$('#footSize').text(intToMb(file.attr('fsize')));
			$('#footDim').text(file.attr('fwidth')+'x'+file.attr('fheight'));
			
			$('#footTableName, #footDateLabel, #footLinkLabel, #footDimLabel, #footDate, #footLink, #footDim').css('visibility','visible');
		}
	}
	
	//Очистить поля информации
	
	//Байты в Мб и Кб
	function intToMb(i) {
		if(i < 1024) {
			return i + ' b';
		} else if(i < 1048576) {
			var v = i/1024;
			v = parseInt(v*10)/10;
			return v + ' kb';
		} else {
			var v = i/1048576;
			v = parseInt(v*10)/10;
			return v + ' Mb';
		}
	}
	
	//Редактировать имя
	$('#fileNameEdit').click(function(){
		$('#fileName').html('<input type="text" name="fileName" id="fileNameValue" value="'+$('#fileName').html()+'" />');
		$('#fileNameValue').focus();
		$('#fileNameEdit').hide();
		$('#fileNameSave').show();
	});
	//Сохранить имя
	$('#fileNameSave').click(function(){
		$('#loader').show();
		
		//Запрос
		//$('.imageBlockAct').attr('filename')
		var path = getCurrentPath();
		var newname = $('#fileNameValue').val();
		$.ajax({
			type: "POST",
			url: "connector/php/",
			data: 'action=renamefile&path='+path.path+'&pathtype='+path.type+'&filename='+$('.imageBlockAct').attr('filename')+'&newname='+newname,
			success: function(data){
				$('#loader').hide();
				if(data != 'error') {
					$('#fileName').html(newname);
					$('.imageBlockAct').attr('fname', newname);
					$('.imageBlockAct .imageName').text(newname);
				} else {
					alert(data);
				}
			}
		});
		
		$('#fileNameSave').hide();
		$('#fileNameEdit').show();
	});
	
	
	//Меню загрузчика
	$('#uploadMenu a').click(function(){
		$('#uploadMenu a').removeClass('act');
		$(this).addClass('act');
		
		if($(this).attr('id') == 'uploadAreaNormalControl') {
			$('#uploadAreaNormal').show();
			$('#uploadAreaMulti').hide();
		} else if($(this).attr('id') == 'uploadAreaMultiControl') {
			$('#uploadAreaNormal').hide();
			$('#uploadAreaMulti').show();
		}
		
		return false;
	});
	
	//Закрыть загрузчик
	$('#uploadClose').click(function(){
		$('#loader').show();
		var path = getCurrentPath();
		$.ajax({
			type: "POST",
			url: "connector/jsp/showtree.jsp",
			data: "action=showtree&path="+path.path+"&type="+path.type,
			success: function(data){
				//$('#loader').hide();
				$('#tree').html(data);
			}
		});
		openFolder(path.type, path.path, function(){ $('#loader').hide(); });
		
		$('#upload').hide();
		$('#divStatus').html('');
	});
	
	//Получить текущую директорию и ее тип
	function getCurrentPath() {
		var type = $('.addrItem:first').attr('pathtype');
		var path = $('.addrItemEnd').attr('path');
		
		if(!path) path = '/';
		
		return {'type':type, 'path':path};
	}

});