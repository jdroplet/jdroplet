<?php

//Site root dir
define('DIR_ROOT',		$_SERVER['DOCUMENT_ROOT']);
//Images dir (root relative)
define('DIR_IMAGES',	'/storage/images');
//Files dir (root relative)
define('DIR_FILES',		'/storage/files');


//Width and height of resized image
define('WIDTH_TO_LINK', 500);
define('HEIGHT_TO_LINK', 500);

//Additional attributes class and rel
define('CLASS_LINK', 'lightview');
define('REL_LINK', 'lightbox');

date_default_timezone_set('Asia/Yekaterinburg');
?>
