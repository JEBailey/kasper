# kasper
Template/DSL for HTML 5 pages

The purpose of kasper came about as an ongoing conversation about the issues related to html and the confusion that occurs when some developers interact with it on only an occasional basis. The problem arises from the inconsistent manner that html is written.

An example of this is in html5 where there is no concept of a self closing tag. Rather a tag that is self closed, in the example of ```<div/>``` is not actually closed since the html5 spec considers ```/>``` the same as ```>``` meaning that ```<div/>``` is translated as an opening tag for a div statement. This means that ```<div/></div>``` is a valid html5 structure.

What we wanted was a formatting that we could use that was consistent, straightforward, and be familiar to a developer so that it would allow them to read it in a straightforward manner. 

Kasper is the result of that conversation.

Kasper works on the following premis
```
a (href='#here') "this is a link"
^       ^               ^
|       |               |
|       |         Body of the tag. If multiple lines of items
|       |         are needed they can be encompassed with curly braces
|       | 
|     Comma seperated list fo attributes
|
Tag identififier
```


sample usage

```js
div (class="foo") {
  a (href='#here') "this is a link"
}
```

produces
```
<div class='foo'>
    <a href='#here'>this is a link</a>
</div>
```
