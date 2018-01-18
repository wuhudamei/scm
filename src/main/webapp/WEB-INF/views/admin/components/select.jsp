<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template id="vueselect2">
<!-- multiple -->
<span v-if="multiple" @click.stop="multiInputFocus"
  v-el:container
  :class="{'select2-container--disabled':disabled}"
  class="select2 select2-container select2-container--default select2-container--below select2-container--focus"
  dir="ltr"
  :style="{width:styleObj.width}">
  <span class="selection">
    <span class="select2-selection select2-selection--multiple">
    <ul class="select2-selection__rendered">
      <li v-for="item in selectedList"
        :title="item.name"
        class="select2-selection__choice">
        <span @click.stop="multiRemoveItem(item,$index,$event)" class="select2-selection__choice__remove">×</span>
        {{item.name}}
      </li>
      <li class="select2-search select2-search--inline">
        <input v-model="search"
          v-el:multi-input
          @blur.stop="inputBlur"
          @focus.stop="inputFocus"
          @keyup.delete.stop="multiRemoveLastItem"
          @keyup.enter.stop="fakeItem"
          @keyup.stop="multiInputKeyup"
          :placeholder="selectedList.length === 0 ? placeholder : ''"
          :style="{width:styleObj.inputWidth}"
          class="select2-search__field" type="search" tabindex="0" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false">
      </li>
    </ul>
    </span>
  </span>
  <span class="dropdown-wrapper"></span>
</span>
<!-- multiple list -->
<span v-if="multiple" v-show="panelShow"
  class="select2-container select2-container--default select2-container--open"
  style="position: absolute;left:0;"
  :style="resultStyleObj">
  <span class="select2-dropdown select2-dropdown--below"
    dir="ltr"
    :style="{width:styleObj.width}">
    <span class="select2-results">
      <ul class="select2-results__options">
        <li v-if="loading"
          class="select2-results__option select2-results__message">正在搜索</li>
        <li v-if="!loading && !results"
          class="select2-results__option select2-results__message">未找到结果</li>
        <li v-if="!loading && results"
          v-for="result in results"
          :class="{'selected':result._selected}"
          @click="multiChoose(result,$index,$event)"
          class="select2-results__option">{{result.name}}</li>
      </ul>
    </span>
  </span>
</span>

<!-- single -->
<div style="position:relative;">
<span v-if="!multiple" @click.stop="singleInputFocus"
  v-el:container
  :class="{
    'select2-container--open':panelShow,
    'select2-container--close':!panelShow,
    'select2-container--disabled':disabled
  }"
  class="select2 select2-container select2-container--default select2-container--below"
  dir="ltr"
  :style="{width:styleObj.width}">
  <span class="selection">
    <span class="select2-selection select2-selection--single">
      <span v-if="selectedList[0]" class="select2-selection__rendered" :title="selectedList[0].name">
        <span v-if="allowClear" @click.stop="singleClearSelected" class="select2-selection__clear">×</span>
        {{selectedList[0].name}}
      </span>
      <span class="select2-selection__arrow" role="presentation"><b role="presentation"></b></span>
    </span>
  </span>
  <span class="dropdown-wrapper" aria-hidden="true"></span>
</span>
<!-- single list -->
<span v-if="!multiple" v-show="panelShow" class="select2-container select2-container--default select2-container--open" style="position: absolute; top: 26px; left: 0px;">
  <span class="select2-dropdown select2-dropdown--below" dir="ltr"
    :style="{width:styleObj.width}">
    <span class="select2-search select2-search--dropdown">
      <input v-el:single-input
        @keyup.stop="singleInputKeyup"
        @click.stop="singleInputClick"
        @focus.stop="inputFocus"
        v-model="search"
        class="select2-search__field"
        type="search"
        autocomplete="off"
        autocorrect="off"
        autocapitalize="off"
        spellcheck="false"
        role="textbox">
    </span>
    <span class="select2-results">
      <ul class="select2-results__options" id="select2-vegs-results">
        <li v-if="loading" class="select2-results__option select2-results__message">正在搜索</li>
        <li v-if="!loading && !results" class="select2-results__option select2-results__message">未找到结果</li>
        <li v-if="!loading && results" v-for="result in results"  @click="singleChoose(result,$index,$event)" :class="{'selected':result._selected}" class="select2-results__option">{{result.name}}</li>
      </ul>
    </span>
  </span>
</span>
</div>
</template>
