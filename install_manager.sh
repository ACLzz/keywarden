#!/bin/bash

if [[ $1 == "link" ]]
then
  echo $2 | sudo -S ln -s ~/.keywarden/bin/keywarden /usr/local/bin/keywarden
fi

if [[ $1 == "unlink" ]]
then
  echo $2 | sudo -S rm /usr/local/bin/keywarden
fi
