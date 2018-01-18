var gulp = require('gulp');
var browserSync = require('browser-sync').create();
var compass = require('gulp-compass');
var autoprefixer = require('gulp-autoprefixer');
var sourcemaps = require('gulp-sourcemaps');
var sass = require('gulp-ruby-sass');


gulp.task('browser-sync-server', function() {
    browserSync.init({
        server: {
            baseDir: 'src/main/webapp/static/'
        }
    });
    browserSync.watch("src/main/webapp/static/**/*.css", function (event, file) {
        if (event === "change") {
            browserSync.reload("*.css");
        }
    });

    browserSync.watch("src/main/webapp/static/**/*.html").on("change", browserSync.reload);

    browserSync.watch("src/main/webapp/static/m/**/*.scss", function(event, file) {
        if (event === "change") {
            var autoprefixerConfig = {
            browsers: ['last 200 versions'],
            cascade: false,
            remove:false //是否去掉不必要的前缀 默认：true
        };

        sass('./src/main/webapp/static/m/sass/**/*.scss',{
                sourcemap:true,
            })
            .pipe(sourcemaps.init())
            .pipe(autoprefixer(autoprefixerConfig))
            .pipe(sourcemaps.write('.'))
            .pipe(gulp.dest('src/main/webapp/static/m/css/'));
        }
    });

    browserSync.watch("src/main/webapp/static/pc/**/*.scss", function(event, file) {
        if (event === "change") {
            // gulp.src('src/main/webapp/static/pc/**/*.scss')
            //     .pipe(compass({
            //       config_file: 'config.rb',
            //       css: 'src/main/webapp/static/pc/css',
            //       sass:'src/main/webapp/static/pc/sass'
            //     }))
            //     .pipe(autoprefixer({
            //         browsers: ['last 20 versions'],
            //         cascade: false,
            //         remove:false //是否去掉不必要的前缀 默认：true
            //     }))
            //     .pipe(gulp.dest('src/main/webapp/static/pc/css/'));
            //----------------------------------------------------------
            var autoprefixerConfig = {
                browsers: ['last 200 versions'],
                cascade: false,
                remove:false //是否去掉不必要的前缀 默认：true
            };

            sass('src/main/webapp/static/pc/sass/**/*.scss',{
                    sourcemap:true,
                })
                .pipe(sourcemaps.init())
                .pipe(autoprefixer(autoprefixerConfig))
                .pipe(sourcemaps.write('.'))
                .pipe(gulp.dest('src/main/webapp/static/pc/css/'));
        }
    });
});

gulp.task('browser-sync-proxy', function() {
    browserSync.init({
        files: [
            'src/main/webapp/WEB-INF/**/*.jsp',
            'src/main/webapp/static/css/**/*.css',
            'src/main/webapp/static/js/**/*.js'
        ],
        proxy: 'http://192.168.1.179:13080'
    });
});

gulp.task('server', ["browser-sync-server"]);

gulp.task('proxy', ["browser-sync-proxy"]);








// const gulp = require('gulp');
const concat = require('gulp-concat');
const uglify = require('gulp-uglify');
const cssnano = require('gulp-cssnano');
const vender_src_prefix = 'src/main/webapp/static/admin/vendor/';
const dest_prefix = './src/main/webapp/static/admin/';
// 设置了环境变量为 production
const isProduction = process.env.NODE_ENV === 'production';



gulp.task('js_lib', function() {
  var array = [
    // vue
    `${vender_src_prefix}/vue/vue.min.js`,
    `${vender_src_prefix}/vue-resource/vue-resource.min.js`,
    `${vender_src_prefix}/vue-validator/vue-validator.min.js`,
    // lodash
    `${vender_src_prefix}/lodash/lodash.min.js`,
    // jquery
    `${vender_src_prefix}/jquery/jquery-2.2.4.min.js`,
    // moment
    `${vender_src_prefix}/moment/moment.min.js`,
    `${vender_src_prefix}/moment/locale/zh-CN.js`,
    // bootstrap
    `${vender_src_prefix}/bootstrap/js/bootstrap.min.js`,
    // bootstrap-modal
    `${vender_src_prefix}/bootstrap-modal/js/bootstrap-modal.js`,
    `${vender_src_prefix}/bootstrap-modal/js/bootstrap-modalmanager.js`,
    // bootstrap-table
    `${vender_src_prefix}/bootstrap-table/bootstrap-table.js`,
    `${vender_src_prefix}/bootstrap-table/bootstrap-table-locale-all.min.js`,
    `${vender_src_prefix}/bootstrap-table/extensions/mobile/bootstrap-table-mobile.min.js`,

    `${vender_src_prefix}/perfect-scrollbar/js/perfect-scrollbar.jquery.js`,

    `${vender_src_prefix}/sweetalert/sweetalert.min.js`,

    // bootstrap-datetimepicker
    `${vender_src_prefix}/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js`,
    // 在此设置了默认中文
    `${vender_src_prefix}/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js`,

    `${vender_src_prefix}/metisMenu/metisMenu.min.js`,
    `${vender_src_prefix}/pace/pace.min.js`,
    `${vender_src_prefix}/toastr/toastr.min.js`,

    // custom
    // 为 vue原型加入$querystring 方法
    `${vender_src_prefix}/vue-querystring/vue-querystring.js`,
    // 为 vue原型加入$toastr 方法
    `${vender_src_prefix}/vue-toastr/vue-toastr.js`,
  ];
  if (isProduction) {
    return gulp.src(array)
      .pipe(concat('lib.js'))
      .pipe(uglify())
      .pipe(gulp.dest(`${dest_prefix}/js/`));
  } else {
    return gulp.src(array)
      .pipe(concat('lib.js'))
      .pipe(gulp.dest(`${dest_prefix}/js/`));
  }

});

gulp.task('css_lib', function() {
  var array = [
    `${vender_src_prefix}/bootstrap/css/bootstrap.css`,
    `${vender_src_prefix}/font-awesome/css/font-awesome.min.css`,
    `${vender_src_prefix}/animate/animate.css`,
    `${vender_src_prefix}/bootstrap-modal/css/bootstrap-modal-bs3patch.css`,
    `${vender_src_prefix}/bootstrap-modal/css/bootstrap-modal.css`,
    `${vender_src_prefix}/bootstrap-table/bootstrap-table.css`,
    `${vender_src_prefix}/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css`,
    `${vender_src_prefix}/perfect-scrollbar/css/perfect-scrollbar.css`,
    `${vender_src_prefix}/sweetalert/sweetalert.css`,
    `${vender_src_prefix}/metisMenu/metisMenu.css`,
    `${vender_src_prefix}/pace/pace.css`,
    `${vender_src_prefix}/toastr/toastr.css`,
  ];
  if (isProduction) {
    return gulp.src(array)
      .pipe(concat('lib.css'))
      .pipe(cssnano())
      .pipe(gulp.dest(`${dest_prefix}/css/`))
  } else {
    return gulp.src(array)
      .pipe(concat('lib.css'))
      .pipe(gulp.dest(`${dest_prefix}/css/`))
  }

});

gulp.task('font_lib', function() {
  return gulp.src([
      `${vender_src_prefix}/bootstrap/fonts/glyphicons-halflings-regular.eot`,
      `${vender_src_prefix}/bootstrap/fonts/glyphicons-halflings-regular.svg`,
      `${vender_src_prefix}/bootstrap/fonts/glyphicons-halflings-regular.ttf`,
      `${vender_src_prefix}/bootstrap/fonts/glyphicons-halflings-regular.woff`,
      `${vender_src_prefix}/bootstrap/fonts/glyphicons-halflings-regular.woff2`,
      `${vender_src_prefix}/font-awesome/fonts/fontawesome-webfont.eot`,
      `${vender_src_prefix}/font-awesome/fonts/fontawesome-webfont.svg`,
      `${vender_src_prefix}/font-awesome/fonts/fontawesome-webfont.ttf`,
      `${vender_src_prefix}/font-awesome/fonts/fontawesome-webfont.woff`,
      `${vender_src_prefix}/font-awesome/fonts/fontawesome-webfont.woff2`,
      `${vender_src_prefix}/font-awesome/fonts/FontAwesome.otf`,
    ])
    .pipe(gulp.dest(`${dest_prefix}/fonts/`))
});