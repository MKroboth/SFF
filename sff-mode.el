;;; sff-mode -- left major mode for editing sff files.
;;; Commentary:
;;; Code:

(defvar sff-mode-hook nil)
(defvar sff-basic-offset 4 "The basic indentation amount.")

(defvar sff-mode-map
  (let ((map (make-keymap)))
    (define-key map "\C-j" 'newline-and-indent)
    map)
  "Keymap for SFF major mode.")

(defconst sff-font-lock-keywords-1
  (let* ((pprop-list "\\(?:(\\(?2:.+\\))\\)")
	(pattr-map "\\(?:\\[\\(?4:.+\\)]\\)")
	(pany-whitespace "[ \t]*")
	(pidentifier "\\(?1:[^#$@\n()[= \t][^#@\n()[=]*\\)")
	(pgroup (concat "\\<" pidentifier pprop-list "?" pattr-map "?" pany-whitespace "{"))
	(pntext (concat "\\<" pidentifier pprop-list "?" pattr-map "?" pany-whitespace "<"))
	(pprop (concat "\\<"
		       pidentifier
		       pprop-list "?"
		       pattr-map "?"
		       pany-whitespace "=" pany-whitespace
		       "\\(?3:\\w.*\\)" "$")))
    (list
     '("#.*$" . font-lock-comment-face)
     '("@.*$" . font-lock-preprocessor-face)
     `(,pgroup (1 font-lock-function-name-face)
	       (2 font-lock-string-face)
	       (4 font-lock-string-face))
     `(,pntext (1 font-lock-function-name-face)
	       (2 font-lock-string-face)
	       (4 font-lock-string-face))
     `(,pprop (1 font-lock-variable-name-face)
	      (2 font-lock-string-face)
	      (4 font-lock-string-face)
	      (3 font-lock-warning-face))))
  "Minimal highlighting expressions for SFF mode.")

(defvar sff-font-lock-keywords sff-font-lock-keywords-1
  "Default syntax highlighting for SFF mode.")

(defun sff-indent-line ()
  "Indent current line as SFF code."
  (interactive)
  (beginning-of-line)
  (if (bobp)
      (indent-line-to 0)
    (let ((not-indented t) cur-indent)
      (if (looking-at "^[ \t]*\\(}\\|>\\)")
	  (progn
	    (save-excursion
	      (forward-line -1)
	      (setq cur-indent (- (current-indentation) sff-basic-offset)))
	    (if (< cur-indent 0)
		(setq cur-indent 0)))
	(save-excursion
	  (while not-indented
	    (forward-line -1)
	    (if (looking-at "^.*\\(}\\|>\\)")
		(progn
		  (setq cur-indent (- (current-indentation) sff-basic-offset))
		  (setq not-indented nil))
	      (if (looking-at "^.*\\({\\|<\\)")
		  (progn
		    (setq cur-indent (+ (current-indentation) sff-basic-offset))
		    (setq not-indented nil))
		(if (bobp)
		    (setq not-indented nil)))))))
      (if cur-indent
	  (indent-line-to cur-indent)
	(indent-line-to 0)))))


(defvar sff-mode-syntax-table
  (let ((st (make-syntax-table)))
    (modify-syntax-entry ?_ "w" st)
    (modify-syntax-entry ?< "|" st)
    (modify-syntax-entry ?> "|" st)
    (modify-syntax-entry ?# "<" st)
    (modify-syntax-entry ?\n ">" st)
    (modify-syntax-entry ?{ "(" st)
    (modify-syntax-entry ?} ")" st)
    (modify-syntax-entry ?\( "(" st)
    (modify-syntax-entry ?\) ")" st)
    (modify-syntax-entry ?\[ "(" st)
    (modify-syntax-entry ?\] ")" st)
    st)
  "Syntax table for sff-mode.")

(defun sff-mode ()
  "Major mode for editoing Cactis SFF files."
  (interactive)
  (kill-all-local-variables)
  (set-syntax-table sff-mode-syntax-table)
  (use-local-map sff-mode-map)
  (set (make-local-variable 'font-lock-defaults) '(sff-font-lock-keywords))
  (set (make-local-variable 'indent-line-function) 'sff-indent-line)
  (set (make-local-variable 'comment-start) "# ")
  (setq major-mode 'sff-mode)
  (setq mode-name "SFF")
  (run-hooks 'sff-mode-hook))

(add-to-list 'auto-mode-alist '("\\.sff\\'" . sff-mode))

(provide 'sff-mode)
;;; sff-mode.el ends here

