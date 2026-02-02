
import { getTeacherStats } from "@/database/queries";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";
import { notFound } from "next/navigation";

export default async function TeacherDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const teacherId = parseInt(id);
  if (isNaN(teacherId)) return notFound();

  const stats = await getTeacherStats(teacherId);

  if (!stats) return notFound();

  // Helper to format date
  const formatDate = (dateInt: number | null) => {
    if (!dateInt) return "N/A";
    const s = dateInt.toString();
    return `${s.slice(6, 8)}.${s.slice(4, 6)}.${s.slice(0, 4)}`;
  };

  return (
    <main className="container mx-auto py-10 px-4 space-y-6">
      <Link href="/teachers">
        <Button variant="ghost" className="mb-4 pl-0 hover:bg-transparent hover:underline">
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to Teachers
        </Button>
      </Link>

      <div className="flex items-center gap-6">
         <div className="w-24 h-24 rounded-full bg-primary flex items-center justify-center text-4xl font-bold text-primary-foreground shadow-lg">
            {stats.code.substring(0, 2)}
         </div>
         <div>
           <h1 className="text-4xl font-bold">{stats.code}</h1>
           <p className="text-muted-foreground">Teacher Profile</p>
         </div>
      </div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Total Absences</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-4xl font-bold">{stats.totalAbsences}</div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Recent Substitutions / Cancellations</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Date</TableHead>
                <TableHead>Period</TableHead>
                <TableHead>Subject</TableHead>
                <TableHead>Info</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {stats.recentEntries.map((entry, idx) => (
                <TableRow key={idx}>
                  <TableCell className="font-medium">{formatDate(entry.date)}</TableCell>
                  <TableCell>{entry.period}</TableCell>
                  <TableCell>{entry.subject}</TableCell>
                  <TableCell>
                    {entry.info && <Badge variant="secondary">{entry.info}</Badge>}
                  </TableCell>
                </TableRow>
              ))}
               {stats.recentEntries.length === 0 && (
                <TableRow>
                  <TableCell colSpan={4} className="text-center py-4 text-muted-foreground">
                    No recent records found.
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
