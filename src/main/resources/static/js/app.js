
$(function() {
    'use strict'

    /**
     * Get access to plugins
     */
    $('[data-toggle="control-sidebar"]').controlSidebar()
    $('[data-toggle="push-menu"]').pushMenu()

    var $pushMenu = $('[data-toggle="push-menu"]').data('lte.pushmenu')
    var $controlSidebar = $('[data-toggle="control-sidebar"]').data('lte.controlsidebar')
    var $layout = $('body').data('lte.layout')

    /**
     * Get a prestored setting
     *
     * @param String name Name of of the setting
     * @returns String The value of the setting | null
     */
    function get(name) {
        if (typeof(Storage) !== 'undefined') {
            return localStorage.getItem(name)
        } else {
            window.alert('Please use a modern browser to properly view this template!')
        }
    }

    /**
     * Store a new settings in the browser
     *
     * @param String name Name of the setting
     * @param String val Value of the setting
     * @returns void
     */
    function store(name, val) {
        if (typeof(Storage) !== 'undefined') {
            localStorage.setItem(name, val)
        } else {
            window.alert('Please use a modern browser to properly view this template!')
        }
    }

    /**
     * Toggles layout classes
     *
     * @param String cls the layout class to toggle
     * @returns void
     */
    function changeLayout(cls) {
        $('body').toggleClass(cls)
        $layout.fixSidebar()
        if ($('body').hasClass('fixed') && cls == 'fixed') {
            $pushMenu.expandOnHover()
            $layout.activate()
        }
        $controlSidebar.fix()
    }

    function initMenu(){
    	$('.sidebar-menu .treeview .treeview-menu a').on('click',function(){
    		var url = $(this).data('url');
    		if(!url) return;
    		$('.sidebar-menu .treeview').removeClass('active menu-open');
    		$('.sidebar-menu .treeview .treeview-menu li').removeClass('active');
    		$(this).closest("li").addClass("active");
    		$(this).parent().parent().closest("li").addClass("active menu-open");
    		
    		store('current-url',url);
    		
    		$('#main-content').load("/" + url);
    	});
    }
    
    
    function bindRefrush(){
//    	 window.onload = function() {   
//           var url = get('current-url');
//           $('#main-content').load("/" + url);
//         }  
    }
    /**
     * Retrieve default settings and apply them to the template
     *
     * @returns void
     */
    function setup() {
        // Add the layout manager
        $('[data-layout]').on('click', function() {
            changeLayout($(this).data('layout'))
        })

        $('[data-controlsidebar]').on('click', function() {
            changeLayout($(this).data('controlsidebar'))
            var slide = !$controlSidebar.options.slide

            $controlSidebar.options.slide = slide
            if (!slide)
                $('.control-sidebar').removeClass('control-sidebar-open')
        })

        $('[data-sidebarskin="toggle"]').on('click', function() {
            var $sidebar = $('.control-sidebar')
            if ($sidebar.hasClass('control-sidebar-dark')) {
                $sidebar.removeClass('control-sidebar-dark')
                $sidebar.addClass('control-sidebar-light')
            } else {
                $sidebar.removeClass('control-sidebar-light')
                $sidebar.addClass('control-sidebar-dark')
            }
        })

        $('[data-enable="expandOnHover"]').on('click', function() {
            $(this).attr('disabled', true)
            $pushMenu.expandOnHover()
            if (!$('body').hasClass('sidebar-collapse'))
                $('[data-layout="sidebar-collapse"]').click()
        })

        //  Reset options
        if ($('body').hasClass('fixed')) {
            $('[data-layout="fixed"]').attr('checked', 'checked')
        }
        if ($('body').hasClass('layout-boxed')) {
            $('[data-layout="layout-boxed"]').attr('checked', 'checked')
        }
        if ($('body').hasClass('sidebar-collapse')) {
            $('[data-layout="sidebar-collapse"]').attr('checked', 'checked')
        }

        //bind  ajax processor.
        $(document).ajaxStart(function() {
            Pace.restart()
        });
        
        //init menu click event
        initMenu();
        
        // bind windows refrush event 
        bindRefrush();
        
        $(document).on('click','[data-mode]',function(){
            var data = $(this).data();
            if(undefined != data['data'] && typeof data['data'] != "object") {
                data['data'] = eval("("+data.data+")");
            }
            $.agile(data);
        });
        
        $(document).on('click','[data-project]',function(){
            var data = $(this).data();
            var projectId = data['project'];
            if(!projectId) return;
            $('#main-content').load("/task/content/" + projectId);
        });
        
    }
    
    setup()
    $('[data-toggle="tooltip"]').tooltip()
})