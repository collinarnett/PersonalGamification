{ pkgs ? import <nixpkgs> {} }: pkgs.mkShell { buildInputs = with pkgs; [ sbt jdk scalafmt ]; }
