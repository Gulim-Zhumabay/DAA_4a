import csv
import os
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

here = os.path.dirname(os.path.abspath(__file__))


def read(name):
    with open(os.path.join(here, name)) as f:
        return list(csv.DictReader(f))


def series(rows, key, value, **filters):
    out = []
    for r in rows:
        if all(r[k] == v for k, v in filters.items()):
            out.append((float(r["n"]), float(r[value])))
    return out


dp = read("dp.csv")
plt.figure(figsize=(7, 4.5))
for name in ["LCS", "EditDistance"]:
    pts = series(dp, "algorithm", "avg_ms", algorithm=name)
    plt.plot([p[0] for p in pts], [p[1] for p in pts], marker="o", label=name)
plt.xlabel("string length n")
plt.ylabel("average time (ms)")
plt.title("lcs and edit distance: time vs input size")
plt.grid(True, alpha=0.3)
plt.legend()
plt.tight_layout()
plt.savefig(os.path.join(here, "plot_dp.png"), dpi=150)
plt.close()

m = read("matching.csv")
fig, axes = plt.subplots(1, 2, figsize=(11, 4.5), sharey=True)
for ax, kind in zip(axes, ["present", "absent"]):
    for name in ["Naive", "KMP", "RabinKarp"]:
        pts = series(m, "algorithm", "avg_ms", algorithm=name, pattern=kind)
        ax.plot([p[0] for p in pts], [p[1] for p in pts], marker="o", label=name)
    ax.set_title("pattern " + kind)
    ax.set_xlabel("text length n")
    ax.grid(True, alpha=0.3)
axes[0].set_ylabel("average time (ms)")
axes[0].legend()
fig.suptitle("string matching: time vs text length")
fig.tight_layout()
fig.savefig(os.path.join(here, "plot_matching.png"), dpi=150)
plt.close(fig)

k = read("kasai.csv")
plt.figure(figsize=(7, 4.5))
pts = series(k, "algorithm", "avg_ms")
plt.plot([p[0] for p in pts], [p[1] for p in pts], marker="o", color="tab:green")
plt.xlabel("string length n")
plt.ylabel("average time (ms)")
plt.title("kasai lcp: time vs input size")
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig(os.path.join(here, "plot_kasai.png"), dpi=150)
plt.close()
