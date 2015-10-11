# kasper
Template/DSL for HTML 5 pages

Provides a code inspired templating mechanism to generate HTML 5 code in a consistent manner

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
