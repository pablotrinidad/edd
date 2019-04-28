import itertools
for i in range(3, 31):
    filename = "graphs_k{}.txt".format(i)
    content = "Grafica # K{}\n".format(i)
    comb = list(itertools.combinations(range(i), 2))
    for c in comb:
        content += "\t" + str(c) + "\n"
    f = open(filename, "w")
    f.write(content)
    f.close()