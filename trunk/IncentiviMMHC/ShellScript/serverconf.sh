#!/bin/bash

ipSorgente=$1
selfish=$2

case "$selfish" in 
yes)
	iptables -N "TAB_$ipSorgente"
	iptables -D INPUT -s 192.168.0.5 -j "TAB_$ipSorgente"
	iptables -A INPUT -s 192.168.0.5 -j "TAB_$ipSorgente"
	iptables -F "TAB_$ipSorgente"
	
	iptables -A "TAB_$ipSorgente" -p tcp --tcp-flags SYN,ACK SYN -j ACCEPT	
	iptables -A "TAB_$ipSorgente" -j DROP
	
	echo "Sono selfish nei confronti di $ipSorgente"
	;;

no)
	iptables -N "TAB_$ipSorgente"
	iptables -D INPUT -s 192.168.0.5 -j "TAB_$ipSorgente"
	iptables -A INPUT -s 192.168.0.5 -j "TAB_$ipSorgente"
	iptables -F "TAB_$ipSorgente"
	
	iptables -A "TAB_$ipSorgente" -j ACCEPT
	
	echo "Sono onesto nei confronti di $ipSorgente"
	;;
	
*)
	echo Usage: $0 'ipSorgente [yes|no]'
	exit 1
	;;
esac



