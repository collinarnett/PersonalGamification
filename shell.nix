{ pkgs ? import <nixpkgs> {}}:
with pkgs;

mkShell {
  buildInputs = [
    mill
    jdk
    scalafmt
  ];
}
