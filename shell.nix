{ pkgs }: pkgs.mkShell { buildInputs = with pkgs; [ sbt jdk scalafmt metals]; }
