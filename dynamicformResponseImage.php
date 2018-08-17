<?php
$jsonurl = "http://www.mocky.io/v2/5b76a7203000002b00848c15";
$json = file_get_contents($jsonurl);
$jsonValue=json_decode($json);
echo json_encode($jsonValue);
?>