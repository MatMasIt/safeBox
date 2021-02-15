# safeBox
A tool to share an archive with important messages in special events.

You can give keys (passwords) to a number of people and have them enter them into the correct order to extract your files

Note:

The archive creation program is not yet done, for now you will have to nanually do it yourself
## Archive Creation
### 1. Create a zip achive
### 2.  Add the "meta.json" file inside
The file should be structured in this way
#### Example
```JSON
{
  "title": "Title",
  "author": "Author",
  "message": "Lorem ipsum\ndolor sit amet",
  "nkeys": 2,
  "unixcreated": 1612619383
}
```
#### Keys
|Key|Description|
|-----|-----|
|title|The Title of the archive|
|author|The Author|
|message|A message shown before the key insertion|
|nkeys|The number of keys|
|unixcreated|The UNIX timestamp (seconds) when this archive was created|
### 3. Create a second zip archive and add the files to be stored
### 4. Password protect said file with a concatenation of all the keys surrounded by "|"
(password1, password 2 -> |password1|password2|)

When you hand over the keys, remember to tell the index the key occupies in the list, starting from 1
in this case, 
1. password1
2. password2
### 5. Add the second archive to the first archive (step 1)
### 6. Change the firt archive's (step 1) extension to .sfbx


On opening the file with SafeBox, the metadata will be shown and when all the keys will have been inserted in the correct order, the folder will be extracted
