clear
%%
double1a = [-.2 -.1 0 .1 .2];
double1b = double1a';
double2 = [-.4 -.3 -.2; -.1 0 .1; .2 .3 .4];
single1a = single(double1a);
single1b = single(double1b);
single2 = single(double2);
%%
logical1a = double1a > 0;
logical1b = double1b > 0;
logical2 = double2 > 0;
%%
double1aSp = sparse(double1a);
logical1aSp = sparse(logical1a);
double1bSp = sparse(double1b);
logical1bSp = sparse(logical1b);
double2sp = sparse(double2);
logical2sp = sparse(logical2);
%%
whos
save test1.mat
