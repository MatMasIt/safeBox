<?php
require ("vendor/autoload.php");
echo "SafeBox v 1.0\n";
echo "Ecryptor\n";
echo ">New Archive\n";
$title = readline("\nTitle: ");
$author = readline("\nAuthor: ");
$line = readline("\nMessage [multiline, \"quit\" to quit]: \n");
$message = "";
do{
    if ($line == "quit") break;
    $message .= $line . "\n";
    $line = readline("");
}
while (trim($message) != "quit");
$nkeys = (int)readline("\nHow many keys? : ");
$keytot = [];
for ($i = 0;$i < $nkeys;$i++)
{
    $keytot[] = trim(readline("Key No. " . ($i + 1) . ": "));
}
echo "\n--------\n";
echo "ATTENTION!\n";
echo "Ask to keep the key and key number to the holder!\n";
echo "The SafeBox can only be decrypted with the exact keys in the exact order, which is:\n";
for ($i = 0;$i < $nkeys;$i++)
{
    echo ($i + 1) . " -> " . $keytot[$i] . "\n";
}
echo "\n--------\n";
do{
    $path = readline("\nInsert box folder path: ");
}
while (!is_dir($path));
$fileName = readline("\nInsert Box Name: ");
$fileName = preg_replace("/[^a-zA-Z0-9]+/", "", $fileName);

$json = json_encode(["title" => $title, "author" => $author, "message" => $message, "nkeys" => $nkeys, "unixcreated" => time() ], JSON_PRETTY_PRINT);

$zipDirHandle = new \PhpZip\ZipFile();
$zipDirHandle->addDir($path);
$zipDirHandle->setPassword("|" . implode("|", $keytot) . "|");
$zipDirHandle->saveAsFile("data.zip");

$safeBoxHandle = new \PhpZip\ZipFile();
$safeBoxHandle->addFromString("meta.json", $json);
$safeBoxHandle->addFile("data.zip");
$safeBoxHandle->saveAsFile($fileName . ".sfbx");

echo "\nDONE\n";
