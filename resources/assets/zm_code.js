  var TABLE_NAME = 'hljs-ln',
	    LINE_NAME = 'hljs-ln-line',
	    CODE_BLOCK_NAME = 'hljs-ln-code',
	    NUMBERS_BLOCK_NAME = 'hljs-ln-numbers',
	    NUMBER_LINE_NAME = 'hljs-ln-n',
		DATA_ATTR_NAME = 'data-line-number';
	
	var format = function (str, args) {
		return str.replace(/\{(\d+)\}/g, function(m, n){
			return args[n] ? args[n] : m;
		});
	};
		
    function lineNumbersBlock (element) {
        var content = element.innerHTML
		var lines = getLines(content);

		if (lines.length > 0) {
			var html = '';

			for (var i = 0, l = lines.length; i < l; i++) {
				html += format(
					'<tr>\
						<td class="{0}">\
							<div class="{1} {2}" {3}="{5}"></div>\
						</td>\
						<td class="{4}">\
							<div class="{1}">{6}</div>\
						</td>\
					</tr>',
				[
					NUMBERS_BLOCK_NAME,
					LINE_NAME,
					NUMBER_LINE_NAME,
					DATA_ATTR_NAME,
					CODE_BLOCK_NAME,
					i + 1,
					lines[i].length > 0 ? lines[i] : ' '
				]);
			}

			element.innerHTML = format('<table class="{0}">{1}</table>', [ TABLE_NAME, html ]);
		}
	}

	function getLines(text) {
		if (text.length === 0) return [];
		return text.split(/\r\n|\r|\n/g);
	}
	
	function documentReady () {
		lineNumbersBlock(document.body)
	}