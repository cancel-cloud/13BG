
import { getTopAbsentTeachers } from "@/database/queries";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

export const revalidate = 60; // Revalidate every minute

export default async function Home() {
  const topTeachers = await getTopAbsentTeachers(50);
  const podium = topTeachers.slice(0, 3);
  const rest = topTeachers.slice(3);

  // Helper to format date
  const formatDate = (dateInt: string | null) => {
    if (!dateInt) return "N/A";
    const s = dateInt.toString();
    return `${s.slice(6, 8)}.${s.slice(4, 6)}.${s.slice(0, 4)}`;
  };

  return (
    <main className="container mx-auto py-10 px-4 space-y-8">
      <div className="text-center space-y-4">
        <h1 className="text-4xl font-extrabold tracking-tight lg:text-5xl bg-gradient-to-r from-primary to-primary/50 bg-clip-text text-transparent">
          Teacher Absence Leaderboard
        </h1>
        <p className="text-xl text-muted-foreground">
          Top ranking of teachers with the most substituted or cancelled lessons.
        </p>
      </div>

      {/* Podium */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-8">
        {podium.map((t, i) => (
          <Card key={t.id} className={`relative overflow-hidden border-2 ${i === 0 ? "border-yellow-500/50 shadow-yellow-500/20 shadow-xl scale-105 z-10" : i === 1 ? "border-zinc-400/50 shadow-zinc-400/20 shadow-lg" : "border-amber-700/50 shadow-amber-700/20 shadow-lg"}`}>
             <div className="absolute top-0 right-0 p-4 opacity-10 font-black text-9xl leading-none select-none">
              {i + 1}
            </div>
            <CardHeader className="text-center pb-2">
              <div className="mx-auto w-24 h-24 rounded-full bg-secondary flex items-center justify-center mb-4 text-3xl font-bold border-4 border-background shadow-sm">
                {t.code.substring(0, 2)}
              </div>
              <CardTitle className="text-2xl">{t.code}</CardTitle>
              <CardDescription>Rank #{i + 1}</CardDescription>
            </CardHeader>
            <CardContent className="text-center">
              <div className="text-4xl font-bold mb-1">{t.absences}</div>
              <p className="text-xs text-muted-foreground uppercase tracking-wider font-semibold">Absences</p>
              
              <div className="mt-6 pt-4 border-t w-full flex justify-between items-center text-sm text-muted-foreground">
                 <span>Last Seen:</span>
                 <Badge variant="outline">{formatDate(t.lastAbsence)}</Badge>
              </div>
            </CardContent>
          </Card>
        ))}
        {/* Fill empty podium slots if needed */}
        {podium.length < 3 && Array.from({ length: 3 - podium.length }).map((_, i) => (
           <Card key={`empty-${i}`} className="opacity-50 border-dashed">
             <CardHeader className="text-center"><CardTitle>No Data</CardTitle></CardHeader>
           </Card>
        ))}
      </div>

      {/* Full List */}
      <Card>
        <CardHeader>
          <CardTitle>Global Ranking</CardTitle>
          <CardDescription>Complete list of teachers by absence count.</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-[100px]">Rank</TableHead>
                <TableHead>Teacher Code</TableHead>
                <TableHead className="text-right">Total Absences</TableHead>
                <TableHead className="text-right">Last Absence</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {rest.map((t, i) => (
                <TableRow key={t.id}>
                  <TableCell className="font-medium">#{i + 4}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <Avatar className="h-8 w-8">
                        <AvatarFallback className="text-xs bg-muted">{t.code.substring(0, 2)}</AvatarFallback>
                      </Avatar>
                      {t.code}
                    </div>
                  </TableCell>
                  <TableCell className="text-right font-bold">{t.absences}</TableCell>
                  <TableCell className="text-right text-muted-foreground">{formatDate(t.lastAbsence)}</TableCell>
                </TableRow>
              ))}
              {topTeachers.length === 0 && (
                <TableRow>
                  <TableCell colSpan={4} className="text-center py-10 text-muted-foreground">
                    No data available.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </main>
  );
}
