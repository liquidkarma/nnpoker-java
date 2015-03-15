all:
	cd src && $(MAKE) $*

%:
	cd src && $(MAKE) $@

# vim:noet
