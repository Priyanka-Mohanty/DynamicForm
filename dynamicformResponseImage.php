<?php
$file_path = "uploads/";

$file_path = $file_path . basename( $_FILES['FileName']['name']);
    if(move_uploaded_file($_FILES['FileName']['tmp_name'], $file_path)) {
        $result = array("result" => "success");
    } else{
        $result = array("result" => "error");
    }
// echo $_FILES['FileName']['name'] . '<br/>';
  // //ini_set('upload_max_filesize', '1000000M');
  // $target_path = "uploads/";
  // $target_path = $target_path . basename($_FILES['FileName']['name']);
  // try {
   // if (!move_uploaded_file($_FILES['FileName']['tmp_name'], $target_path)) {
  // throw new Exception('Could not move file');
  // }

 echo "Image uploaded";
  } catch (Exception $e) {
  die('Image did not upload: ' . $e->getMessage());
  }
  //https://android.jlelse.eu/how-to-upload-image-to-mysql-server-using-retrofit-and-ion-in-android-observer-pattern-part-1-51c2340241c0
$jsonurl = "http://www.mocky.io/v2/5b76a7203000002b00848c15";
$json = file_get_contents($jsonurl);
$jsonValue=json_decode($json);
echo json_encode($jsonValue);
?>