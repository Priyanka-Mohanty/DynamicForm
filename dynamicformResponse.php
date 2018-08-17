<?php
$jsonurl = "http://www.mocky.io/v2/5b7554352e00005100535fe3";
$json = file_get_contents($jsonurl);
$jsonValue=json_decode($json);
echo json_encode($jsonValue);
?>